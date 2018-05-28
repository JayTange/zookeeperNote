package com.zkclient;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class ZkNode {
    static String zkIp = "192.168.90.53:2181";
    static String path = "/tang";

    public static void main(String args[]) throws Exception {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zkIp)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
//        createNode(client);
//        deleteNode(client);
//        getData(client);
        updateData(client);
    }

    /**
     * 创建节点
     *
     * @param client
     * @throws Exception
     */
    public static void createNode(CuratorFramework client) throws Exception {
        client.create().forPath(path, "test".getBytes());
    }

    /**
     * 删除节点
     *
     * @param client
     * @throws Exception
     */
    public static void deleteNode(CuratorFramework client) throws Exception {
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(path);
        client.delete().deletingChildrenIfNeeded().withVersion(stat.getAversion()).forPath(path);
    }

    /**
     * 获取数据
     * @param client
     * @throws Exception
     */
    public static void  getData(CuratorFramework client) throws Exception{
        Stat stat = new Stat();
        byte[] result = client.getData().storingStatIn(stat).forPath(path);
        System.out.println("获取的数据为："+new String(result));
    }

    /**
     * 更新数据
     * @param client
     * @throws Exception
     */
    public static void updateData(CuratorFramework client) throws Exception{
        Stat stat = new Stat();
        byte[] result = client.getData().storingStatIn(stat).forPath(path);
        System.out.println("获取的数据为："+new String(result));
        client.setData().withVersion(stat.getVersion()).forPath(path,"new".getBytes());
    }
}
