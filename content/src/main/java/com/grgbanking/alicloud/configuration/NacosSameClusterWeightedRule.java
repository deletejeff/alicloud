//package com.grgbanking.alicloud.configuration;
//
//import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
//import com.alibaba.cloud.nacos.ribbon.NacosServer;
//import com.alibaba.nacos.api.exception.NacosException;
//import com.alibaba.nacos.api.naming.NamingService;
//import com.alibaba.nacos.api.naming.pojo.Instance;
//import com.alibaba.nacos.client.naming.core.Balancer;
//import com.netflix.client.config.IClientConfig;
//import com.netflix.loadbalancer.AbstractLoadBalancerRule;
//import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
//import com.netflix.loadbalancer.Server;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.CollectionUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * 优先选择同一集群下的服务节点访问
// * 此类功能等同于：com.alibaba.cloud.nacos.ribbon.NacosRule
// * @author machao
// */
//@Configuration
//public class NacosSameClusterWeightedRule extends AbstractLoadBalancerRule {
//
//    public static final Logger logger = LoggerFactory.getLogger(NacosSameClusterWeightedRule.class);
//
//    @Autowired
//    private NacosDiscoveryProperties nacosDiscoveryProperties;
//    /**
//     * Concrete implementation should implement this method so that the configuration set via
//     * {@link IClientConfig} (which in turn were set via Archaius properties) will be taken into consideration
//     *
//     * @param clientConfig
//     */
//    @Override
//    public void initWithNiwsConfig(IClientConfig clientConfig) {
//
//    }
//
//    @Override
//    public Server choose(Object key) {
//        try {
//            List<Instance> chosenInstance = new ArrayList<>();
//            //拿到当前的集群名称
//            String clusterName = nacosDiscoveryProperties.getClusterName();
//            DynamicServerListLoadBalancer loadBalancer = (DynamicServerListLoadBalancer) getLoadBalancer();
//            //想要请求的微服务名称
//            String name = loadBalancer.getName();
//            //拿到服务发现相关的API
//            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
//            //1.找出指定服务下的所有实例
//            List<Instance> allInstances = namingService.selectInstances(name, true);
//            //2.过滤出相同集群下的所有实例
//            List<Instance> sameClusterInstances = allInstances.stream()
//                    .filter(instance -> Objects.equals(instance.getClusterName(), clusterName))
//                    .collect(Collectors.toList());
//            //3.如果相同集群下实例为空，就用找出的所有实例
//            if(!CollectionUtils.isEmpty(sameClusterInstances)){
//                chosenInstance = sameClusterInstances;
//            }else{
//                logger.info("发生跨集群的调用， name = {}, clusterName = {}, instances = {}" ,
//                        name,
//                        clusterName,
//                        allInstances);
//                chosenInstance = allInstances;
//            }
//            //4.基于权重的负载均衡算法，返回1个实例
////            Instance ins = namingService.selectOneHealthyInstance(name);
//            Instance instance = ExtendBalancer.myGetHostByRandomWeight(chosenInstance);
//            logger.info("选择的实例是 clusterName = {}, port = {}, instance = {}",
//                    instance.getClusterName(),
//                    instance.getPort(),
//                    instance);
//            return new NacosServer(instance);
//        } catch (NacosException e) {
//            logger.error("nacos权重选择器出现异常！", e);
//        }
//        return null;
//    }
//}
//
//class ExtendBalancer extends Balancer {
//    public static Instance myGetHostByRandomWeight(List<Instance> hosts) {
//        return getHostByRandomWeight(hosts);
//    }
//}
