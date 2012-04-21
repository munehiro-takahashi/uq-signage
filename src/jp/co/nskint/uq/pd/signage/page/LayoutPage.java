/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.page;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;
import org.slim3.util.LongUtil;

import jp.co.nskint.uq.pd.signage.model.Layout;
import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.xml.ComponentType;
import jp.co.nskint.uq.pd.signage.model.xml.LayoutXml;
import jp.co.nskint.uq.pd.signage.model.xml.StreamVideo;
import jp.co.nskint.uq.pd.signage.model.xml.StreamVideoType;
import jp.co.nskint.uq.pd.signage.service.LayoutService;
import jp.co.nskint.uq.pd.signage.service.ManagerService;
import jp.co.nskint.uq.pd.signage.util.NumberingParamaterMap;
import scenic3.annotation.ActionPath;
import scenic3.annotation.Default;
import scenic3.annotation.Page;
import scenic3.annotation.RequestParam;

/**
 * レイアウト操作画面
 *
 */
@Page("/layout")
public class LayoutPage extends BasePage {
    private LayoutService lService = new LayoutService();
    private ManagerService mService = new ManagerService();
    
    @Default
    public Navigation list(@RequestParam("mid") String mid){
        final String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.entering(this.getClass().getName(), methodName);
//        // 管理者とマネージャ以外は権限エラー
//        if( !checkAdmin() && !checkManager()) {
//            errors.put("page", ApplicationMessage.get("error.authority"));
//            return forward("/error.jsp");
//        }
        
        try {
            List<Layout> layoutList = new ArrayList<Layout>();
            
            // マネージャIDが与えられていない場合
            if(mid == null || mid.isEmpty()) {
                errors.put("page", "マネージャIDが指定されていません。");
                return forward("/error.jsp");
            }
            
            Manager manager = (Manager)mService.get(mid);
            
            if(manager == null) {
                errors.put("page", "指定されたマネージャは存在しません。");
                return forward("/error.jsp");
            }
            
            layoutList =  manager.getLayoutListRef().getModelList();
            
            request.setAttribute("layoutList", layoutList);
            request.setAttribute("manager", manager);
            
            return forward("/layout/list.jsp");
        }
        finally {
            logger.exiting(this.getClass().getName(), methodName);
        }
    }
    
    /**
     * レイアウト編集画面を表示するアクション
     * @return
     */
    @ActionPath("edit")
    public Navigation edit(@RequestParam("mid") String mid, @RequestParam("lid") long lid) {
        final String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.entering(this.getClass().getName(), methodName);
        
//        this.requestDump();
        
        try {
            Manager manager = (Manager)mService.get(mid);
            if(manager == null) {
                errors.put("page", "指定されたマネージャは存在しません。");
                return forward("/error.jsp");
            }
            
            LayoutXml layoutXml = null;
            // 新規登録の場合
            if(lid < 1) {
                layoutXml = new LayoutXml();
                
                // DEBUG 
                StreamVideo video = new StreamVideo();
                video.setUrl("http://www.ustream.tv/embed/10065127");
                video.setHeight(400);
                video.setWidth(600);
                video.setX(40);
                video.setY(100);
                video.setType(StreamVideoType.UST);
                
                layoutXml.getComponents().add(video);
            }
            // 更新の場合
            else {
                Layout layout = lService.get(manager, lid);
                
                // レイアウトXMLを取得
                if(layout != null) {
                    layoutXml = layout.getXmlModel();
                }
                else {
                    layoutXml = new LayoutXml();
                }
            }
            
            this.request.setAttribute("mid", mid);
            this.request.setAttribute("lid", lid);
            this.request.setAttribute("layout", layoutXml);
            
            return forward("/layout/edit.jsp");
        }
        finally {
            logger.exiting(this.getClass().getName(), methodName);
        }
    }
    
    
    /**
     * レイアウトを保存するアクション
     * @return 
     */
    @ActionPath("save")
    public Navigation save(
            @RequestParam("mid") String mid,
            @RequestParam("lid") long lid,
            @RequestParam("sum") int sum) {
        final String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.entering(this.getClass().getName(), methodName);
        
        try {
//            this.requestDump();
            
            Manager manager = (Manager)mService.get(mid);
            if(manager == null) {
                errors.put("page", "指定されたマネージャは存在しません。");
                return forward("/error.jsp");
            }
            
            Date now = new Date();
            LayoutXml layoutXml = new LayoutXml();
            
            for(int i = 0; i < sum; i++) {
                // クラス名からインスタンスを生成する。
                String className = this.request.getParameter(i + "_ComponentClassName");
                
                if( className == null || className.isEmpty() ) {
                    continue;
                }
                
                try {
                    @SuppressWarnings("unchecked")
                    Class<ComponentType> clazz = (Class<ComponentType>) Class.forName(className);
                    
                    if( clazz == null) {
                        continue;
                    }
                    
                    ComponentType component = clazz.newInstance();
                    NumberingParamaterMap map = new NumberingParamaterMap(this.request);
                    map.setNumber(i);
                    BeanUtil.copy(map, component);
                    layoutXml.getComponents().add(component);
                }
                catch(ClassNotFoundException e) {
                    logger.info("クラスが存在しません。");
                } catch (InstantiationException e) {
                    logger.info("インスタンスが作成できません。");
                } catch (IllegalAccessException e) {
                    logger.info("権限がありません。");
                }
            }
            // 保存
            Layout layout = null;
            
            // 新規登録の場合
            if(lid < 1) {
                layout = new Layout();
                layout.setId(LayoutService.createNewKey());
                layout.setRegisteredDate(now);
            }
            // 更新の場合
            else {
                layout = lService.get(manager, lid);
            }
            
            layout.setXmlModel(layoutXml);
            layout.setUpdatedDate(now);
            
//            manager.getLayoutListRef().getModelList().add(layout);
            layout.setManagerRef(manager);
            lService.put(manager, layout);
            
            // 編集画面に遷移する。
            return redirect("/layout/edit?mid=" + manager.getUid().getName()+"&lid=" + layout.getId().getId());
        }
        finally {
            logger.exiting(this.getClass().getName(), methodName);
        }
    }
    
    /**
     * DEBUG用
     * リクエストパラメータを全て表示する。
     */
    private void requestDump() {
        @SuppressWarnings("unchecked")
        Enumeration<String> names = this.request.getParameterNames();
        StringBuffer sb = new StringBuffer();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            sb.append("\n");
            sb.append(name);
            sb.append(": ");
            
            String[] values = this.request.getParameterValues(name);
            if( values == null ) {
                sb.append("null");
                continue;
            }
            StringBuffer v = new StringBuffer();
            for(int i = 0; i < values.length; i++) {
                if(0 < v.length()) {
                    v.append(", ");
                }
                v.append(values[i]);
            }
            sb.append(v);
        }
        logger.info(sb.toString());
    }
    
    @ActionPath("createmanager")
    public Navigation createmanager() {
        ManagerService service = new ManagerService();
        service.put(
            "testmanager",
            "テストマネージャ",
            "test@localhostdomain.jp",
            "0587-000-0000",
            "4820043",
            "愛知県岩倉市本町");
        service.savePassword("testmanager", "password");
        return redirect("/layout/edit");
    }
    
    @ActionPath("delete")
    public Navigation delete(@RequestParam("mid") String mid) {
        final String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.entering(this.getClass().getName(), methodName);
        
        try {
//            this.requestDump();
            
            Manager manager = (Manager)mService.get(mid);
            if(manager == null) {
                errors.put("page", "指定されたマネージャは存在しません。");
                return forward("/error.jsp");
            }
            
            String[] values = this.request.getParameterValues("lids");
            
            List<Long> lids = new ArrayList<Long>();
            
            if(values != null) {
                for( String value : values) {
                    try{
                        lids.add( LongUtil.toLong(value));
                    }
                    catch(NumberFormatException e) {
                        continue;
                    }
                }
                
                lService.delete(manager, lids);
            }
            
            // 編集画面に遷移する。
            return redirect("/layout/?mid=" + mid);
        }
        finally {
            logger.exiting(this.getClass().getName(), methodName);
        }
    }
}
