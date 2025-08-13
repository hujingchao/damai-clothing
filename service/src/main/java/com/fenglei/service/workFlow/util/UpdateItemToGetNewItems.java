package com.fenglei.service.workFlow.util;
import com.fenglei.common.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * User: yzy
 * Date: 2019/7/4 0004
 * Time: 11:21
 * Description: No Description
 */
public class UpdateItemToGetNewItems {

    public static List<?> get(List<String> oldItIds, List<String> newItIds, List oldItems, Class classz) {
        List<String> itIds = new ArrayList<>(oldItIds);
        itIds.retainAll(newItIds);
        List newItems = new ArrayList();
        for (String id : itIds) {
            for (Object item : oldItems) {
                Object olId = FileUtil.getFieldValueByClass("id", item);
                if (id.equals(olId)) {
                    if(classz.isInstance(item)){
                        if (geto(classz, item) != null) {
                            newItems.add(geto(classz, item));
                        }
                    }
                }
            }
        }
        return newItems;
    }

    public static  <T> T geto(Class<T> clz,Object o){
        if(clz.isInstance(o)){
            return clz.cast(o);
        }
        return null;
    }

    public static List<String> getIds(List items) {
        List<String> ids = new ArrayList<>();
        for (Object item : items) {
            String olId =  (String)FileUtil.getFieldValueByClass("id", item);
            ids.add(olId);
        }
        return ids;
    }

    public static List<String> needDelIds(List items, List qitems) {
        List<String> oldItIds = getIds(items);
        List<String> newItIds = getIds(qitems);
        List<String> itIds = new ArrayList<>(oldItIds);
        itIds.retainAll(newItIds);
        oldItIds.removeAll(itIds);
        return oldItIds;
    }

    public static List<String> needDelIdsTwo(List<String> itemIds, List<String> qitemIds) {
        List<String> oldItIds = itemIds;
        List<String> newItIds = qitemIds;
        List<String> itIds = new ArrayList<>(oldItIds);
        itIds.retainAll(newItIds);
        oldItIds.removeAll(itIds);
        return oldItIds;
    }

    public static List<String> difference(List<String> ids1, List<String> Ids2) {
        List<String> itIds = new ArrayList<>(ids1);
        itIds.retainAll(Ids2);
        ids1.removeAll(itIds);
        return ids1;
    }

    public static  List<String> needAddIds(List<String> itemIds, List<String> qitemIds) {
        List<String> oldItIds = itemIds;
        List<String> newItIds = qitemIds;
        List<String> itIds = new ArrayList<>(newItIds);
        itIds.retainAll(oldItIds);
        newItIds.removeAll(itIds);
        return newItIds;
    }
}
