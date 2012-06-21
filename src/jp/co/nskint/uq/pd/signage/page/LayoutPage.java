/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.page;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
//import java.util.Enumeration;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.BeanUtil;
import org.slim3.util.LongUtil;

import jp.co.nskint.uq.pd.signage.model.Layout;
import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.xml.ComponentType;
import jp.co.nskint.uq.pd.signage.model.xml.LayoutXml;
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
    /** 初期レイアウト名 */
    private static final String DEFAULT_NAME = "名称未設定";

    /** 初期幅 */
    private static final int DEFAULT_WIDTH = 1024;

    /** 初期高さ */
    private static final int DEFAULT_HEIGHT = 768;

    private String XML_PACKAGE = "jp.co.nskint.uq.pd.signage.model.xml";
    private LayoutService lService = new LayoutService();
    private ManagerService mService = new ManagerService();

    /**
     * 指定の代表者が所有するレイアウト一覧を表示するアクション
     * @param mid 代表者ID
     * @return
     */
    @Default
    public Navigation list(@RequestParam("mid") String mid){
        final String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.entering(this.getClass().getName(), methodName);

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
            request.setAttribute(
                "title",
                ApplicationMessage.get("title.layout.list"));

            return forward("/layout/list.jsp");
        }
        finally {
            logger.exiting(this.getClass().getName(), methodName);
        }
    }

    /**
     * レイアウト編集画面を表示するアクション
     * @param mid 代表者ID
     * @param lid レイアウトID
     * @return
     */
    @ActionPath("edit")
    public Navigation edit(@RequestParam("mid") String mid, @RequestParam("lid") long lid) {
        final String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.entering(this.getClass().getName(), methodName);

        try {
            Manager manager = (Manager)mService.get(mid);
            if(manager == null) {
                errors.put("page", "指定されたマネージャは存在しません。");
                return forward("/error.jsp");
            }

            Layout layout = null;
            LayoutXml layoutXml = null;
            // 新規登録の場合
            if(lid < 1) {
                layout = new Layout();
                layoutXml = new LayoutXml();
                layoutXml.setName(DEFAULT_NAME);
                layoutXml.setHeight(DEFAULT_HEIGHT);
                layoutXml.setWidth(DEFAULT_WIDTH);
            }
            // 更新の場合
            else {
                layout = lService.get(manager, lid);

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
            this.request.setAttribute("layout", layout);
            this.request.setAttribute("layoutXml", layoutXml);
            request.setAttribute(
                "title",
                ApplicationMessage.get("title.layout.edit"));

            return forward("/layout/edit.jsp");
        }
        finally {
            logger.exiting(this.getClass().getName(), methodName);
        }
    }


    /**
     * レイアウトを保存するアクション
     * @param mid 代表者ID
     * @param lid レイアウトID
     * @param sum コンポーネント総数
     * @return
     */
    @ActionPath("save")
    public Navigation save(
            @RequestParam("mid") String mid,
            @RequestParam("lid") long lid,
            @RequestParam("sum") int sum,
            @RequestParam("layoutName") String layoutName,
            @RequestParam("width") int width,
            @RequestParam("height") int height) {
        final String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.entering(this.getClass().getName(), methodName);

        try {
            this.requestDump();

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
                    Class<ComponentType> clazz = (Class<ComponentType>) Class.forName(XML_PACKAGE + "." + className);

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

            layoutXml.setName(layoutName);
            layoutXml.setWidth(width);
            layoutXml.setHeight(height);
            layout.setXmlModel(layoutXml);
            layout.setUpdatedDate(now);

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

//    @ActionPath("createmanager")
//    public Navigation createmanager() {
//        ManagerService service = new ManagerService();
//        service.put(
//            "testmanager",
//            "テストマネージャ",
//            "test@localhostdomain.jp",
//            "0587-000-0000",
//            "4820043",
//            "愛知県岩倉市本町");
//        service.savePassword("testmanager", "password");
//        return redirect("/layout/edit");
//    }

    /**
     * レイアウトを削除するアクション
     * @param mid 代表者ID
     * @return
     */
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
