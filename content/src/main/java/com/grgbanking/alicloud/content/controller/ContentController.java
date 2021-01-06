package com.grgbanking.alicloud.content.controller;

import com.alibaba.fastjson.JSON;
import com.grgbanking.alicloud.common.entity.content.ContentEntity;
import com.grgbanking.alicloud.common.entity.user.UserEntity;
import com.grgbanking.alicloud.common.mq.UserAddPointsMqMsg;
import com.grgbanking.alicloud.common.po.content.ContentPo;
import com.grgbanking.alicloud.content.service.ContentService;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author machao
 */
@RestController
@RequestMapping("/content")
public class ContentController {
//    @Autowired
//    private RestTemplate restTemplate;
    @Autowired
    private ContentService contentService;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private Source source;

    public static final Logger logger = LoggerFactory.getLogger(ContentController.class);

    @GetMapping("/getByContentId/{contentId}")
    public ContentPo getById(@PathVariable String contentId) {
        ContentPo contentPo = contentService.getByContentId(contentId);


//        //获取user服务的所有部署实例
//        List<ServiceInstance> userInstances = discoveryClient.getInstances("user");
//        //找出所有实例中第一个uri返回
//        String url = userInstances.stream()
//                .map(instance -> instance.getUri().toString() + "/user/user/getUserByKey/" + contentPo.getUserid())
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("参数错误"));
//        //获取所有实例下的url
//        List<String> urlList = userInstances.stream()
//                .map(instance -> instance.getUri().toString() + "/user/user/getUserByKey/" + contentPo.getUserid())
//                .collect(Collectors.toList());
//        //取urlList的size随机数当作下标
//        int index = ThreadLocalRandom.current().nextInt(urlList.size());
//        logger.info("调用地址为：{}", urlList.get(index));
//        UserEntity userEntity = restTemplate.getForObject(urlList.get(index), UserEntity.class);



//        //使用restTemplate调用user项目
//        //http://user/user/user/getUserByKey/{userid}
//        // 第一个user为nacos的服务名称
//        // 第二个user为项目访问根路径
//        // 第三个user为userController对应的url
//        UserEntity userEntity = restTemplate.getForObject("http://user/user/user/getUserByKey/{userid}"
//                , UserEntity.class
//                , contentPo.getUserid());


        return contentPo;
    }

    /**
     * 添加评论，并增加积分，通过feignClient调用
     * @param contentPo
     * @return
     */
    @PostMapping("/addContent")
    public Map<String,Object> addContent(@RequestBody ContentPo contentPo){
        Map<String, Object> map = new HashMap<>(2);
        try {
            ContentEntity contentEntity = new ContentEntity();
            BeanUtils.copyProperties(contentPo, contentEntity);
            boolean res = contentService.addContentByFeignClient(contentEntity);
            if(res){
                map.put("errorCode", 0);
                map.put("errorMsg", "success");
            }else{
                map.put("errorCode", -1);
                map.put("errorMsg", "failure");
            }
        } catch (Exception e) {
            map.put("errorCode", -1);
            map.put("errorMsg", "failure");
            logger.error("content data insert failure!", e);
        }
        return map;
    }

    /**
     * 添加评论，并增加积分，通过rocketMq调用
     * @param contentPo
     * @return
     */
    @PostMapping("/addContentByMq")
    public Map<String,Object> addContentByMq(@RequestBody ContentPo contentPo){
        Map<String, Object> map = new HashMap<>(2);
        try {
            ContentEntity contentEntity = new ContentEntity();
            BeanUtils.copyProperties(contentPo, contentEntity);
            boolean res = contentService.addContentByMq(contentEntity);
            if(res){
                map.put("errorCode", 0);
                map.put("errorMsg", "success");
            }else{
                map.put("errorCode", -1);
                map.put("errorMsg", "failure");
            }
        } catch (Exception e) {
            map.put("errorCode", -1);
            map.put("errorMsg", "failure");
            logger.error("content data insert failure!", e);
        }
        return map;
    }

    /**
     * 添加评论，并增加积分，通过rocketMq调用，增加事务
     * @param contentPo
     * @return
     */
    @PostMapping("/addContentByMqTransaction")
    public Map<String,Object> addContentByMqTransaction(@RequestBody ContentPo contentPo){
        Map<String, Object> map = new HashMap<>(2);
        try {
            ContentEntity contentEntity = new ContentEntity();
            contentEntity.setContentId(UUID.randomUUID().toString().replace("-", ""));
            BeanUtils.copyProperties(contentPo, contentEntity);
            boolean res = contentService.addContentByMqTransaction(contentEntity);
            if(res){
                map.put("errorCode", 0);
                map.put("errorMsg", "success");
            }else{
                map.put("errorCode", -1);
                map.put("errorMsg", "failure");
            }
        } catch (Exception e) {
            map.put("errorCode", -1);
            map.put("errorMsg", "failure");
            logger.error("content data insert failure!", e);
        }
        return map;
    }

    /**
     * 添加内容数据,通过rocketMq添加积分,使用cloud stream
     * @param contentPo
     * @return
     */
    @PostMapping("/addContentByCloudStream")
    public Map<String,Object> addContentByCloudStream(@RequestBody ContentPo contentPo){
        Map<String, Object> map = new HashMap<>(2);
        try {
            ContentEntity contentEntity = new ContentEntity();
            contentEntity.setContentId(UUID.randomUUID().toString().replace("-", ""));
            BeanUtils.copyProperties(contentPo, contentEntity);
            String transactionId = UUID.randomUUID().toString();
            logger.info("生成的 transactionId = {}", transactionId);
            UserAddPointsMqMsg userAddPointsMqMsg = new UserAddPointsMqMsg();
            userAddPointsMqMsg.setUserid(contentEntity.getUserid());
            userAddPointsMqMsg.setPoints(50);
            userAddPointsMqMsg.setDescription("通过mqCloudStream消息，评论获取积分");
            userAddPointsMqMsg.setEvent("ADD");
            source.output().send(MessageBuilder
                    .withPayload(userAddPointsMqMsg)
                    .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                    .setHeader("contentId", contentEntity.getContentId())
                    .setHeader("dto", JSON.toJSONString(userAddPointsMqMsg))
                    .setHeader("contentEntity", JSON.toJSONString(contentEntity))
                    .setHeader("filterHeader","just do it")
                    .build());
            boolean res = true;
            if(res){
                map.put("errorCode", 0);
                map.put("errorMsg", "success");
            }else{
                map.put("errorCode", -1);
                map.put("errorMsg", "failure");
            }
        } catch (Exception e) {
            map.put("errorCode", -1);
            map.put("errorMsg", "failure");
            logger.error("content data insert failure!", e);
        }
        return map;
    }

    public static void main(String[] args) {
        List<UserEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUserid(String.valueOf(i));
            userEntity.setUsername(String.valueOf(i) + "name");
            list.add(userEntity);
        }

        List<String> collect = list.stream()
                .map(userEntity -> userEntity.getUsername())
                .collect(Collectors.toList());
        collect.forEach(str -> System.out.println(str));

    }
}
