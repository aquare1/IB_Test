package com.aquare.common;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author dengtao aquare@163.com
 */
@Repository
@Mapper
public interface BaseDao<Entity extends BaseEntity> {

    //按任意字段查询所有字段内容
    @SelectProvider(type = Provider.class, method = "findAll")
    List<Entity> findAll(Entity entity);

    //按任意字段查询所有字段内容并指定时间段和时间字段
    @SelectProvider(type = Provider.class, method = "findAllByTimePicker")
    List<Entity> findAllByTimePicker(Entity entity, String timePickerFieldName, Timestamp startTime, Timestamp endTime);

    //按任意字段模糊查询所有字段内容
    @SelectProvider(type = Provider.class, method = "likeAll")
    List<Entity> likeAll(Entity entity);

    //按任意字段去重复查询
    @SelectProvider(type = Provider.class, method = "destinctAll")
    List<Object> destinctAll(final Entity entity);

    @SelectProvider(type = Provider.class, method = "likeDistinct")
        //按任意字段模糊查询包含条件内容的结果并去除重复项
    List<Entity> likeDistinct(Entity entity);

    //按任意字段匹配多个值
    @SelectProvider(type = Provider.class, method = "inAll")
    List<Entity> inAll(Entity entity);

    //以条件查询统计结果数量
    @SelectProvider(type = Provider.class, method = "countByAll")
    int countByAll(Entity entity);

    @InsertProvider(type = Provider.class, method = "add")//添加数据，并且添加之后将新增的id返回结果集中
    //@SelectKey(statement="call next value for TestSequence", keyProperty="id", before=false, resultType=int.class)//oracle
    //@SelectKey(statement = "SELECT LAST_INSERT_ID() AS id", keyProperty = "id", before = false, resultType = Long.class)//MySQL

    //修改数据，以id为识别标识，修改其他字段
    @UpdateProvider(type = Provider.class, method = "update")
    int updateById(Entity entity);

    //保存数据，以id为空则新增，id不为空则以id识别标识，修改其他字段
    @UpdateProvider(type = Provider.class, method = "save")
    int save(Entity entity);

    //删除数据，以所有字段为筛选条件
    @DeleteProvider(type = Provider.class, method = "deleteByAll")
    int deleteByAll(Entity entity);

    // 安全删除，以所有字段为筛选条件
    @UpdateProvider(type = Provider.class, method = "safeDeleteByAll")
    int safeDeleteByAll(Entity entity);

    // 恢复安全删除，以所有字段为筛选条件
    @UpdateProvider(type = Provider.class, method = "recoverSafeDeleteByAll")
    int recoverSafeDeleteByAll(Entity entity);

    // 以下是mysql版本
    @Log4j2
    class Provider<Entity extends BaseEntity> {

        static Pattern p = Pattern.compile("[A-Z]");

        static char xChar = '_';

        static String xString = "_";

        /**
         * 大写字母转换为下划线加小写
         * @param param 输入变量字符串
         * @return 带下划线的字段名
         */
        public static String camel4underline(String param) {

            if (param == null || param.isEmpty()) {
                return "";
            }
            StringBuilder builder = new StringBuilder(param);
            Matcher mc = p.matcher(param);
            int i = 0;
            while (mc.find()) {
                builder.replace(mc.start() + i, mc.end() + i, xString + mc.group().toLowerCase());
                i++;
            }

            if (xChar == builder.charAt(0)) {
                builder.deleteCharAt(0);
            }
            return builder.toString();
        }

        public String countByAll(final Entity entity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            String sql = new SQL() {{
                if (entity != null) {
                    SELECT("COUNT(0)");
                    //FROM(entity.getClass().getSimpleName());
                    FROM(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                    for (Field field : entity.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        if (field.get(entity) != null && !String.valueOf(field.get(entity)).isEmpty()) {
                            WHERE(camel4underline(field.getName())  + " = #{" + field.getName() + "}");
                        }
                    }
                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            log.info("sql:" + sql);
            return sql;
        }

        public String findAll(final Entity entity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            String sql = new SQL() {{
                if (entity != null) {
                    //FROM(entity.getClass().getSimpleName());
                    FROM(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                    for (Field field : entity.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        SELECT(camel4underline(field.getName()) );
                        if (field.get(entity) != null && !String.valueOf(field.get(entity)).isEmpty()) {
                            WHERE(camel4underline(field.getName()) + " = #{" + field.getName() + "}");
                        }
                    }
                    ORDER_BY("id desc");
                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            log.info("sql:" + sql);
            return sql;
        }

        public String findAllByTimePicker(final Entity entity, String timePickerFieldName, Timestamp startTime, Timestamp endTime)
                throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            String sql = new SQL() {{
                if (entity != null) {
                    //FROM(entity.getClass().getSimpleName());
                    FROM(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                    HashMap<String, Field> fieldMapByfieldName = new HashMap<String, Field>();
                    for (Field field : entity.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        SELECT(camel4underline(field.getName()));
                        if (field.get(entity) != null && !String.valueOf(field.get(entity)).isEmpty()) {
                            WHERE(camel4underline(field.getName())  + " = #{entity." + field.getName() + "}");
                        }
                        fieldMapByfieldName.put(field.getName(), field);
                    }
                    if (fieldMapByfieldName.containsKey(timePickerFieldName)) {
                        WHERE(timePickerFieldName + " >='" + startTime + "'  and " + timePickerFieldName + " < '" + endTime + "'");
                    }
                    ORDER_BY("id desc");
                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            log.info("sql:" + sql);
            return sql;
        }

        public String countByTimePicker(Entity entity, String timePickerFieldName, Timestamp startTime, Timestamp endTime)
                throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            String sql = new SQL() {{
                if (entity != null) {
                    SELECT("COUNT(0) as count");
                    //FROM(entity.getClass().getSimpleName());
                    HashMap<String, Field> fieldMapByfieldName = new HashMap<String, Field>();
                    FROM(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                    for (Field field : entity.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        if (field.get(entity) != null && !String.valueOf(field.get(entity)).isEmpty()) {
                            SELECT(camel4underline(field.getName())  + " as field");
                            GROUP_BY(camel4underline(field.getName()));
                        }
                        fieldMapByfieldName.put(field.getName(), field);
                    }
                    if (fieldMapByfieldName.containsKey(timePickerFieldName)) {
                        WHERE(timePickerFieldName + " >='" + startTime + "'  and " + timePickerFieldName + " < '" + endTime + "'");

                    }

                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            log.info("sql:" + sql);
            return sql;
        }

        public String destinctAll(final Entity entity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            String sql = new SQL() {{
                if (entity != null) {
                    //FROM(entity.getClass().getSimpleName());
                    FROM(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                    for (Field field : entity.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        if (field.get(entity) != null && !String.valueOf(field.get(entity)).isEmpty()) {
                            SELECT_DISTINCT(camel4underline(field.getName()) );
                        }
                    }
                    ORDER_BY("id desc");
                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            log.info("sql:" + sql);
            return sql;
        }

        public String inAll(final Entity entity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            String sql = new SQL() {{
                if (entity != null) {
                    //FROM(entity.getClass().getSimpleName());
                    FROM(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                    for (Field field : entity.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        SELECT(camel4underline(field.getName()));
                        if (field.get(entity) != null && !String.valueOf(field.get(entity)).isEmpty()) {
                            WHERE(camel4underline(field.getName()) + " in (" + String.valueOf(field.get(entity)) + ")");
                        }
                    }
                    ORDER_BY("id desc");
                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            log.info("sql:" + sql);
            return sql;
        }

        public String findData(final Entity entity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            String sql = new SQL() {{
                if (entity != null) {
                    SELECT("*");
                    //FROM(entity.getClass().getSimpleName());
                    FROM(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                    for (Field field : entity.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        if (field.get(entity) != null && !String.valueOf(field.get(entity)).isEmpty()) {
                            WHERE(camel4underline(field.getName()) + " = #{" + field.getName() + "}");
                        }
                    }
                    ORDER_BY("AcquisitionTime desc limit 0,30");
                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            log.info("sql:" + sql);
            return sql;
        }

        public String likeAll(final Entity entity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            String sql = new SQL() {{
                if (entity != null) {
                    //FROM(entity.getClass().getSimpleName());
                    FROM(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                    for (Field field : entity.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        SELECT(camel4underline(field.getName()));
                        if (field.get(entity) != null && !String.valueOf(field.get(entity)).isEmpty()) {
                            WHERE("LOCATE(#{" + field.getName() + "}, " + camel4underline(field.getName()) + ")>0");
                        }
                    }
                    ORDER_BY("id desc");
                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            log.info("sql:" + sql);
            return sql;
        }

        public String likeDistinct(final Entity entity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            String sql = new SQL() {{
                if (entity != null) {
                    //FROM(entity.getClass().getSimpleName());
                    FROM(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                    for (Field field : entity.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        SELECT_DISTINCT(camel4underline(field.getName()));
                        if (field.get(entity) != null && !String.valueOf(field.get(entity)).isEmpty()) {
                            WHERE("LOCATE(#{" + field.getName() + "}, " + camel4underline(field.getName()) + ")>0");
                        }
                    }
                    ORDER_BY("id desc");
                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            log.info("sql:" + sql);
            return sql;
        }

        public String update(final Entity entity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            //entity.setLastUpdate(new Date());
            String sql = new SQL() {{
                if (entity != null) {
                    if (entity.getId() != null) {
                        //UPDATE(entity.getClass().getSimpleName());
                        UPDATE(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                        for (Field field : entity.getClass().getDeclaredFields()) {
                            field.setAccessible(true);
                            if ("id".equals(field.getName())) {
                                continue;
                            }
                            SET(field.getName() + " = #{" + field.getName() + "}");
                        }
                        WHERE("id=#{id}");
                    }
                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            log.info("sql:" + sql);
            return sql;
        }

        public String save(final Entity entity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            //entity.setLastUpdate(new Date());
            String sql = new SQL() {{
                if (entity != null) {
                    if (entity.getId() != null) {
                        UPDATE(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                        for (Field field : entity.getClass().getDeclaredFields()) {
                            field.setAccessible(true);
                            if (field.get(entity) != null && !"id".equals(field.getName())) {
                                SET(camel4underline(field.getName()) + " = #{" + field.getName() + "}");
                            }
                        }
                        WHERE("id=#{id}");
                    } else {
                        entity.setId(IdUtils.getNextId());
                        INSERT_INTO(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                        for (Field field : entity.getClass().getDeclaredFields()) {
                            field.setAccessible(true);
                            if (field.get(entity) != null ) {
                                INTO_COLUMNS(camel4underline(field.getName()));
                                INTO_VALUES("#{" + field.getName() + "}");
                            } else {
                                INTO_COLUMNS(camel4underline(field.getName()));
                                INTO_VALUES("null");
                            }
                        }
                    }
                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            log.info("sql:" + sql);
            //log.info(JSONChange.objToJson(entity));
            return sql;
        }

        public String add(final Entity entity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            //entity.setLastUpdate(new Date());
            String sql = new SQL() {{
                if (entity != null) {
                    //INSERT_INTO(entity.getClass().getSimpleName());
                    entity.setId(IdUtils.getNextId());
                    INSERT_INTO(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                    for (Field field : entity.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        if (field.get(entity) != null && !String.valueOf(field.get(entity)).isEmpty() && !"id".equals(field.getName())) {
                            INTO_COLUMNS(camel4underline(field.getName()));
                            INTO_VALUES("#{" + field.getName() + "}");
                        } else {
                            INTO_COLUMNS(camel4underline(field.getName()));
                            INTO_VALUES("null");
                        }
                    }
                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            log.info("sql:" + sql);
            //log.info(JSONChange.objToJson(entity));
            return sql;
        }

        public String deleteByAll(final Entity entity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            String sql = new SQL() {{
                if (entity != null && entity.getId() != null) {
                    DELETE_FROM(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                    int count = 0;
                    for (Field field : entity.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        if (field.get(entity) != null && !String.valueOf(field.get(entity)).isEmpty()) {
                            WHERE("" + camel4underline(field.getName()) + " = #{" + field.getName() + "}");
                            count++;
                        }
                    }
                    if (count == 0) {
                        WHERE("id= #{id}");
                    }
                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            log.info("sql:" + sql);
            return sql;
        }

        public String safeDeleteByAll(final Entity entity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            String sql = new SQL() {{
                if (entity != null && entity.getId() != null) {
                    UPDATE(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                    for (Field field : entity.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        if (field.get(entity) != null && !"delFlag".equals(field.getName()) && !"id".equals(field.getName())) {
                            SET("" + camel4underline(field.getName()) + " = #{" + field.getName() + "}");
                        }
                    }
                    WHERE("id= #{id}");
                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            //log.info("sql:" + sql);
            //log.info(JSONChange.objToJson(entity));
            return sql;
        }

        public String recoverSafeDeleteByAll(final Entity entity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            //entity.setLastUpdate(new Date());
            String sql = new SQL() {{
                if (entity != null && entity.getId() != null) {

                    //UPDATE(entity.getClass().getSimpleName());
                    UPDATE(String.valueOf(entity.getClass().getMethod("getMapTabel", null).invoke(entity)));
                    for (Field field : entity.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        if (field.get(entity) != null && "delFlag".equals(field.getName()) && !"id".equals(field.getName())) {
                            SET("" + camel4underline(field.getName()) + " = #{" + field.getName() + "}");
                        }
                    }
                    WHERE("id= #{id}");
                } else {
                    log.error("entity is null:");
                }
            }}.toString();
            log.info("sql:" + sql);
            return sql;
        }

    }

}
