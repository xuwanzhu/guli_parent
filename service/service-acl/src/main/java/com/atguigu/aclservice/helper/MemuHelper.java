package com.atguigu.aclservice.helper;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.aclservice.entity.Permission;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 根据权限数据构建登录用户左侧菜单数据
 * </p>
 *
 */
public class MemuHelper {

    /**
     * 构建菜单
     * @param treeNodes
     * @return
     */
    public static List<JSONObject> bulid(List<Permission> treeNodes) {
        //meuns里面放置的是一级菜单的集合，即权限管理，课程管理等等/ ] JSONObject实现类Map接口，我认为直接使用Map也是可以的
        List<JSONObject> meuns = new ArrayList<>();
        // List<Permission>里面就一个Permission对象，毕竟只有一个权限的pid是o

        if(treeNodes.size() == 1) {
            //获得上述的那个Permission对象，name是"全部数据"
            Permission topNode = treeNodes.get(0);

            //获得左侧一级菜单,即权限管理、讲师管理等等
            List<Permission> oneMeunList = topNode.getChildren();

            //逐个遍历左侧一级菜单(例如:讲师管理)
            for(Permission one :oneMeunList) {
                JSONObject oneMeun = new JSONObject();

                oneMeun.put("path", one.getPath());
                oneMeun.put("component", one.getComponent());
                oneMeun.put("redirect", "noredirect");
                oneMeun.put("name", "name_"+one.getId());
                oneMeun.put("hidden", false);

                JSONObject oneMeta = new JSONObject();
                oneMeta.put("title", one.getName());
                oneMeta.put("icon", one.getIcon());
                oneMeun.put("meta", oneMeta);


                //children是一级父路由的children，用来装二级菜单和三级按钮
                List<JSONObject> children = new ArrayList<>();

                List<Permission> twoMeunList = one.getChildren();//比如讲师管理下的讲师列表、添加讲师...
                for(Permission two :twoMeunList) {
                    JSONObject twoMeun = new JSONObject();

                    twoMeun.put("path", two.getPath());
                    twoMeun.put("component", two.getComponent());
                    twoMeun.put("name", "name_"+two.getId());
                    twoMeun.put("hidden", false);

                    JSONObject twoMeta = new JSONObject();
                    twoMeta.put("title", two.getName());
                    twoMeun.put("meta", twoMeta);

                    // 把二级菜单信息放在一级父路由的children中
                    children.add(twoMeun);

                    //获得二级菜单下面的三级按钮列表，因为我们没有三级菜单，二级都是按钮
                    List<Permission> threeMeunList = two.getChildren();

                    //遍历单个三级按钮
                    for(Permission three :threeMeunList) {
                        //如果三级按钮为空，那就不用返回到前端
                        if(StringUtils.isEmpty(three.getPath())) continue;

                        JSONObject threeMeun = new JSONObject();
                        threeMeun.put("path", three.getPath());
                        threeMeun.put("component", three.getComponent());
                        threeMeun.put("name", "name_"+three.getId());
                        threeMeun.put("hidden", true);

                        JSONObject threeMeta = new JSONObject();
                        threeMeta.put("title", three.getName());
                        threeMeun.put("meta", threeMeta);

                        //把三级按钮放入一级父路由的children中，比如单个banner列表
                        children.add(threeMeun);
                    }
                }
                //hildren中放着二级菜单和三级按钮，我们把children放入一级菜单中二级菜单和三级按钮中的都是普通的信息.，没有children
                oneMeun.put("children", children);
                //把所有一级菜单放在列表中
                meuns.add(oneMeun);
            }

        }
        return meuns;
    }
}
