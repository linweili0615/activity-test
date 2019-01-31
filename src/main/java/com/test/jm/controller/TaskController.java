package com.test.jm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.jm.domain.*;
import com.test.jm.domain.page.TaskPage;
import com.test.jm.dto.ApiDTO;
import com.test.jm.dto.TaskDTO;
import com.test.jm.dto.TaskExtendDTO;
import com.test.jm.dto.TaskJob;
import com.test.jm.keys.ResultType;
import com.test.jm.service.RequestService;
import com.test.jm.service.TaskJobService;
import com.test.jm.service.TaskService;
import com.test.jm.util.RedisUtil;
import com.test.jm.util.UserThreadLocal;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@RequestMapping("/task")
@RestController
public class TaskController {

    //超时时间 40秒
    private static final int TIMEOUT = 40 * 1000;

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskJobService taskJobService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/getLog")
    public Result getlog(@RequestBody String id){
        if(StringUtils.isBlank(id)){
            return new Result(ResultType.ERROR,"taskid不能为空");
        }
        String logname = new File("").getAbsolutePath() + "/task/" + id +"/" + UserThreadLocal.getUserInfo().getUser_id()+"/task.log";
        try {
            File file = new File(logname);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String s = "";
            List list = new ArrayList();
            while ((s = bufferedReader.readLine()) != null){
                if(StringUtils.isNotBlank(s.trim())){
                    list.add(s);
                }
            }
            reader.close();
            return new Result(ResultType.SUCCESS,null,list);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new Result(ResultType.ERROR,e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(ResultType.ERROR,e.getMessage());
        }
    }

    @RequestMapping("/getResult")
    public Result getresult(@RequestBody String id){
        if(StringUtils.isBlank(id)){
            return new Result(ResultType.FAIL,"taskid不能为空");
        }
        String fullPath = "task/" + File.separator +id +  File.separator + UserThreadLocal.getUserInfo().getUser_id() +   File.separator + "result.json";
        try {
            File file = new File(fullPath);
            if(!file.exists()){
                return new Result(ResultType.NOT_FOUND,"该任务结果不存在");
            }
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String s = "";
            StringBuffer buffer = new StringBuffer();
            while ((s = bufferedReader.readLine()) != null){
                if(StringUtils.isNotBlank(s.trim())){
                    buffer.append(s);
                }
            }
            reader.close();
            return new Result(id, ResultType.SUCCESS,null,buffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new Result(ResultType.NOT_FOUND,e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(ResultType.ERROR,e.getMessage());
        }
    }

    @RequestMapping("/test")
    public TaskExtendResult runCase(@RequestBody String task_id){
        if(StringUtils.isBlank(task_id)){
            return new TaskExtendResult(ResultType.FAIL, "task_id不能为空");
        }
//        long time = System.currentTimeMillis() + TIMEOUT;
//        String key = task_id + UserThreadLocal.getUserInfo().getUser_id();
//        if(!redisUtil.lock(key, String.valueOf(time))){
//            return new TaskExtendResult(ResultType.FAIL, "操作太频繁了哈");
//        }
        try {

            List<HttpClientResult> list = requestService.runCase(task_id);
            if(null != list && list.size()>0){
                return new TaskExtendResult(task_id, ResultType.SUCCESS, "task执行成功", list);
            }
            return new TaskExtendResult(task_id, ResultType.FAIL, "task执行失败");
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskExtendResult(task_id, ResultType.ERROR, "task执行异常");
        }finally {
//            redisUtil.unlock(key, String.valueOf(time));
        }
    }

    @PostMapping("/extend/info")
    public TaskExtendResult getTaskExtendInfo(@RequestBody String id){
        if(StringUtils.isBlank(id)){
            return new TaskExtendResult(ResultType.FAIL,"任务ID不能为空");
        }
        try {
            TaskExtendDTO taskExtendDTO = new TaskExtendDTO();
            taskExtendDTO.setTask_id(id);
            List<TaskExtendDTO> data = taskService.getTaskExtendListById(taskExtendDTO);
            if(null != data && data.size()>0){
                return new TaskExtendResult(id, ResultType.SUCCESS,"获取任务详情成功",data);
            }
            return new TaskExtendResult(id, ResultType.SUCCESS,"任务详情无记录");
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskExtendResult(id, ResultType.ERROR, e.getMessage());
        }
    }

    @PostMapping("/extend/status")
    public TaskExtendResult modifyTaskExtendStatus(@RequestBody TaskExtendStatusParams params){
        if(null == params.getList() && params.getList().size() ==0){
            return new TaskExtendResult(ResultType.FAIL, "任务ID不能为空");
        }
        try {
            Integer count = taskService.updateTaskExtendStatusList(params);
            if(count > 0){
                return new TaskExtendResult(ResultType.SUCCESS, "更新任务集合status成功");
            }
            return new TaskExtendResult(ResultType.FAIL, "更新任务集合status失败");
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskExtendResult(ResultType.ERROR, e.getMessage());
        }

    }

    @PostMapping("/extend/deal")
    public TaskExtendResult dealTaskExtend(@RequestBody TaskExtendRank taskExtendRank){
        if(null == taskExtendRank){
            return new TaskExtendResult(ResultType.FAIL, "任务详情信息不能为空");
        }
        List<TaskExtendDTO> list = new ArrayList<>();
        TaskExtendDTO oldTaskExtend = new TaskExtendDTO();
        TaskExtendDTO newTaskExtend = new TaskExtendDTO();
        oldTaskExtend.setId(taskExtendRank.getOld_id());
        oldTaskExtend.setRank(taskExtendRank.getNew_rank());
        newTaskExtend.setId(taskExtendRank.getNew_id());
        newTaskExtend.setRank(taskExtendRank.getOld_rank());
        list.add(oldTaskExtend);
        list.add(newTaskExtend);
        try {
            Integer count = taskService.updateTaskExtendRankByList(list);
            if(count > 0){
                return new TaskExtendResult(ResultType.SUCCESS, "任务详情顺序修改成功");
            }
            return new TaskExtendResult(ResultType.FAIL, "任务详情顺序修改失败");
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskExtendResult(ResultType.ERROR, e.getMessage());
        }
    }


    @PostMapping("/extend/del")
    public TaskExtendResult delTaskInfoById(@RequestBody List<String> list){
        if(null == list && list.size() == 0){
            return new TaskExtendResult(ResultType.FAIL, "任务ID列表不能为空");
        }
        try {
            Integer count = taskService.deleteTaskExtendByList(list);
            if(count > 0){
                return new TaskExtendResult(ResultType.SUCCESS, "任务详情删除成功");
            }
            return new TaskExtendResult(ResultType.FAIL, "任务详情删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskExtendResult(ResultType.ERROR, e.getMessage());
        }
    }

    @PostMapping("/extend/add")
    public TaskExtendResult addTaskInfo(@RequestBody TaskExtendParams params){
        if(StringUtils.isBlank(params.getTask_id())){
            return new TaskExtendResult(ResultType.FAIL, "task_id不能为空");
        }
        if(null == params.getList()){
            return new TaskExtendResult(ResultType.FAIL, "步骤信息不能为空");
        }
        Integer count = 0;
        for (ApiDTO apiDTO: params.getList()) {
            try {
                TaskExtendDTO tt = new TaskExtendDTO();
                tt.setTask_id(params.getTask_id());
                tt.setProject_id(apiDTO.getProject_id());
                tt.setCase_id(apiDTO.getCase_id());
                tt.setApi_id(apiDTO.getId());
                tt.setApi_name(apiDTO.getName());
                TaskExtendDTO ts = taskService.getTaskExtendById(params.getTask_id());
                if(null != ts){
                    tt.setRank(ts.getRank() + 1);
                }else {
                    tt.setRank(1);
                }
                tt.setStatus("1");
                Integer cc = taskService.addTaskExtend(tt);
                if(cc > 0){
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        if(count > 0){
            return new TaskExtendResult(ResultType.SUCCESS, "添加步骤信息成功");
        }
        return new TaskExtendResult(ResultType.FAIL, "添加步骤信息失败");
    }

    @PostMapping("/deal")
    public Result addTask(@RequestBody TaskDTO task){

        if(StringUtils.isBlank(task.getStart_time()) || StringUtils.isBlank(task.getEnd_time())){
            return new Result(ResultType.FAIL,"任务执行时间段不能为空");
        }
        if(StringUtils.isBlank(task.getCron_expression())){
            return new Result(ResultType.FAIL,"任务定时策略不能为空");
        }
        if(StringUtils.isBlank(task.getStatus())){
            return new Result(ResultType.FAIL,"任务状态不能为空");
        }
        if(!CronExpression.isValidExpression(task.getCron_expression())){
            return new Result(ResultType.FAIL,"请输入正确的定时策略");
        }

        if(StringUtils.isBlank(task.getId())){
            if(StringUtils.isBlank(task.getName())){
                return new Result(ResultType.FAIL,"任务名称不能为空");
            }
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(UUID.randomUUID().toString());
            taskDTO.setName(task.getName());
            taskDTO.setStart_time(task.getStart_time());
            taskDTO.setEnd_time(task.getEnd_time());
            taskDTO.setCron_expression(task.getCron_expression());
            taskDTO.setStatus(task.getStatus());
            try {
                log.info("开始写入任务信息");
                Integer count = taskService.addTask(taskDTO);
                if(count > 0){
                    taskJobService.create_job(taskDTO);
                    log.info("成功写入任务信息: {}",taskDTO.getId());
                    return new Result(ResultType.SUCCESS,taskDTO.getId());
                }
                log.info("写入任务信息失败: {}",taskDTO.toString());
                return new Result(ResultType.FAIL,"添加任务失败",taskDTO.toString());
            } catch (Exception e) {
                e.printStackTrace();
                log.info("写入任务信息异常: {}",e.getMessage());
                return new Result(ResultType.ERROR,"添加定时任务成功，更新任务信息失败",taskDTO.getId());
            }
        }
        try {
            TaskDTO t1 = taskService.getTaskById(task.getId());
            if(t1 != null){
                log.info("开始更新任务信息:{}",task.getId());
                Integer count = taskService.updateTask(task);
                if(count > 0){
                    log.info("任务信息已更新,开始更新定时任务策略...");
                    taskJobService.update_job(task);
                    return new Result(ResultType.SUCCESS,"任务信息已更新");
                }else {
                    log.info("更新任务信息失败:{}",task.getId());
                    return new Result(ResultType.FAIL,"更新任务信息失败");
                }
            }
            return new Result(ResultType.FAIL,"请输入正确的任务的ID");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultType.ERROR,e.getMessage());
        }

    }

    @PostMapping("/list")
    public TaskExtendResult getTaskList(@RequestBody TaskPage taskPage){
        if(null == taskPage){
            return new TaskExtendResult(ResultType.FAIL,"请求参数不能为空");
        }
        try {
            Integer pageSize = taskPage.getPageSize();
            Integer pageNo = taskPage.getPageNo();
            if(null == pageSize){
                pageSize = 30;
            }
            if(null == pageNo){
                pageNo = 1;
            }
            PageHelper.startPage(pageNo,pageSize);
            List<TaskJob> data = taskService.getTaskList(taskPage);
            PageInfo<TaskJob> pageInfo = new PageInfo<>(data);
            int row_count = (int) pageInfo.getTotal();
            int pageCount = row_count % pageSize==0 ? row_count/pageSize : row_count/pageSize + 1;
            log.info("获取任务列表成功");
            if(data != null && data.size()>0){
                return new TaskExtendResult(ResultType.SUCCESS,"获取任务列表成功",pageInfo.getTotal(), pageSize, pageCount, pageNo,data);
            }
            log.info("暂无任务列表信息");
            return new TaskExtendResult(ResultType.SUCCESS,"暂无任务列表信息");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取任务列表异常: {}",e.getMessage());
            return new TaskExtendResult(ResultType.ERROR,e.getMessage());
        }
    }
    @PostMapping("/status")
    public Result modifyTaskStatus(@RequestBody TaskDTO task){
        if(null == task){
            return new Result(ResultType.FAIL, "任务信息不能为空");
        }
        if(StringUtils.isBlank(task.getId()) || StringUtils.isBlank(task.getStatus().toString())){
            return new Result(ResultType.FAIL, "任务ID或状态信息不能为空");
        }
        try {
            Integer count = taskService.updateTaskStatus(task);
            if(count > 0){
                if(task.getStatus().equals("1")){
                    taskJobService.create_job(task);
                }else {
                    taskJobService.delete_job(task.getId());
                }
                return new Result(ResultType.SUCCESS,"更新任务status成功");
            }
            return new Result(ResultType.FAIL, "更新任务status失败");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultType.ERROR, e.getMessage());
        }
    }

    @PostMapping("/info")
    public Result getTaskInfo(@RequestBody String id){
        if(StringUtils.isBlank(id)){
            return new Result(ResultType.FAIL, "任务ID不能为空");
        }
        try {
            TaskDTO taskDTO = taskService.getTaskById(id);
            if(taskDTO !=null){
                return new Result(ResultType.SUCCESS,"定时任务已删除",taskDTO);
            }
            return new Result(ResultType.FAIL, "任务ID不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultType.ERROR, e.getMessage());
        }

    }


    @PostMapping("/del")
    public Result delTaskStatus(@RequestBody String id){
        if(StringUtils.isBlank(id)){
            return new Result(ResultType.FAIL, "任务ID不能为空");
        }
        try {
            Integer count = taskService.delTaskById(id);
            if(count > 0){
                log.info("任务：{}已删除",id);
                taskService.delTaskExtendByTaskId(id);
                log.info("任务详情：{}已删除",id);
                if(taskJobService.isJobExists(id)){
                    log.info("开始删除定时策略相关：{}...",id);
                    taskJobService.delete_job(id);
                    log.info("定时策略相关已删除:{}...",id);
                }
                return new Result(ResultType.SUCCESS,"定时任务已删除");
            }
            log.info("定时任务删除失败:{}",id);
            return new Result(ResultType.FAIL, "删除任务失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("定时任务删除异常:{}{}",id,e.getMessage());
            return new Result(ResultType.ERROR, e.getMessage());
        }

    }

}
