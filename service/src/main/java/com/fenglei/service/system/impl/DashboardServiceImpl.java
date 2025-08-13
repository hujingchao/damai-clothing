package com.fenglei.service.system.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.mapper.system.DashboardMapper;
import com.fenglei.model.system.dto.DashboardInvInfo;
import com.fenglei.model.system.dto.DashboardMoInfo;
import com.fenglei.model.system.dto.DashboardProductionInfo;
import com.fenglei.service.system.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl extends ServiceImpl<DashboardMapper, Object> implements DashboardService {

    /**
     * 获取生产信息
     */
    @Override
    public DashboardProductionInfo getProductionInfo() {

        // 今日完成数量
        BigDecimal todayFinishedQty = this.baseMapper.getTodayFinishedQty();
        // 在制量
        BigDecimal inProductionQty = this.baseMapper.getInProductionQty();
        // 今日薪资
        BigDecimal todaySalary = this.baseMapper.getTodaySalary();

        DashboardProductionInfo dashboardProductionInfo = new DashboardProductionInfo();
        dashboardProductionInfo.setTodayFinishedQty(todayFinishedQty);
        dashboardProductionInfo.setInProductionQty(inProductionQty);
        dashboardProductionInfo.setTodaySalary(todaySalary);
        return dashboardProductionInfo;
    }

    /**
     * 查询前十库存数据
     */
    @Override
    public List<DashboardInvInfo> listTop10InvInfo() {
        return this.baseMapper.listTop10InvInfo();
    }

    /**
     * 查询生产订单量
     */
    @Override
    public DashboardMoInfo listMoCount(String dateType) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String beginDate = "";
        String endDate = "";
        if (dateType.equals("本周")) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            beginDate = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");

            calendar.setWeekDate(calendar.getWeekYear(), calendar.get(Calendar.WEEK_OF_YEAR) + 1, Calendar.SUNDAY);
            calendar.set(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    23, 59, 59);
            endDate = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
        } else if (dateType.equals("本月")) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            beginDate = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDate = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
        } else if (dateType.equals("本季度")) {
            LocalDate today = LocalDate.now();
            Month month = today.getMonth();
            Month firstMonthOfQuarter = month.firstMonthOfQuarter();

            // 确定季度起始月份
            int startMonth = firstMonthOfQuarter.getValue();

            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR); // 获取当前年份
            // 设置到本季度的第一天
            calendar.clear(); // 清除其他字段
            calendar.set(Calendar.YEAR, currentYear);
            calendar.set(Calendar.MONTH, startMonth - 1); // 月份是从0开始的
            calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为该月第一天

            // 输出本季度第一天
            beginDate = sdf.format(calendar.getTime());

            // 设置到本季度的最后一天
            calendar.set(Calendar.MONTH, startMonth + 2); // 下个季度的第一月
            calendar.set(Calendar.DAY_OF_MONTH, 0); // 设置为该月最后一天

            // 输出本季度最后一天
            endDate = sdf.format(calendar.getTime());
        } else if (dateType.equals("本年")) {
            LocalDate firstDayOfYear = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
            LocalDate lastDayOfYear = LocalDate.now().with(TemporalAdjusters.lastDayOfYear());
            beginDate = firstDayOfYear.toString();
            endDate = lastDayOfYear.toString();
        } else {
            throw new BizException("查询方式不正确！");
        }
        List<DashboardMoInfo> dashboardMoInfos = this.baseMapper.listMoCount(beginDate, endDate);
        Map<String, Integer> dateCountMap = new LinkedHashMap<>();
        if (dateType.equals("本周")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(beginDate));
            for (int i = 0; i < 7; i++) {
                String key = sdf.format(calendar.getTime());
                List<DashboardMoInfo> collect = dashboardMoInfos.stream().filter(t -> t.getDate().equals(key)).collect(Collectors.toList());
                dateCountMap.put(key, collect.stream().reduce(0, (a, b) -> a + b.getCount(), Integer::sum));
                calendar.add(Calendar.DATE, 1);
            }
        } else if (dateType.equals("本月")) {
            List<Map<String, String>> lastDaysOfMonthWeeks = this.getLastDaysOfMonthWeeks(beginDate);
            for (Map<String, String> lastDaysOfMonthWeek : lastDaysOfMonthWeeks) {
                String key = "第" + lastDaysOfMonthWeek.get("order") + "周";
                dateCountMap.put(key, 0);
                for (DashboardMoInfo dashboardMoInfo : dashboardMoInfos) {
                    String date = dashboardMoInfo.getDate();
                    if (sdf.parse(date).getTime() <= sdf.parse(lastDaysOfMonthWeek.get("end")).getTime() && sdf.parse(date).getTime() >= sdf.parse(lastDaysOfMonthWeek.get("start")).getTime()) {
                        dateCountMap.put(key, dateCountMap.get(key) + dashboardMoInfo.getCount());
                    }
                }
            }
        } else if (dateType.equals("本季度")) {
            List<Map<String, String>> lastDaysOfMonth = this.getPerMonthLastDaysOfQuarter();
            for (Map<String, String> last : lastDaysOfMonth) {
                String key = last.get("order");
                dateCountMap.put(key, 0);
                for (DashboardMoInfo dashboardMoInfo : dashboardMoInfos) {
                    String date = dashboardMoInfo.getDate();
                    if (sdf.parse(date).getTime() <= sdf.parse(last.get("end")).getTime() && sdf.parse(date).getTime() >= sdf.parse(last.get("start")).getTime()) {
                        dateCountMap.put(key, dateCountMap.get(key) + dashboardMoInfo.getCount());
                    }
                }
            }
        } else if (dateType.equals("本年")) {
            List<Map<String, String>> perMonthLastDaysOfYear = this.getPerMonthLastDaysOfYear();
            for (Map<String, String> last : perMonthLastDaysOfYear) {
                String key = last.get("order");
                dateCountMap.put(key, 0);
                for (DashboardMoInfo dashboardMoInfo : dashboardMoInfos) {
                    String date = dashboardMoInfo.getDate();
                    if (sdf.parse(date).getTime() <= sdf.parse(last.get("end")).getTime() && sdf.parse(date).getTime() >= sdf.parse(last.get("start")).getTime()) {
                        dateCountMap.put(key, dateCountMap.get(key) + dashboardMoInfo.getCount());
                    }
                }
            }
        }

        List<String> dateList = new ArrayList<>(dateCountMap.keySet());
        List<Integer> countList = new ArrayList<>(dateCountMap.values());
        DashboardMoInfo res = new DashboardMoInfo();
        res.setDateList(dateList);
        res.setCountList(countList);
        return res;
    }

    /**
     * 获取本年每月的第一天和最后一天
     */
    List<Map<String, String>> getPerMonthLastDaysOfYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        List<Map<String, String>> res = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);

            Map<String, String> temp = new HashMap<>();
            String firstDayOfMonth  = String.format("%tF", calendar);
            temp.put("start", firstDayOfMonth );

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            String lastDayOfMonth = String.format("%tF", calendar);
            temp.put("end", lastDayOfMonth);

            temp.put("order", (i + 1) +"月");
            res.add(temp);

            calendar.add(Calendar.MONTH, 1);
        }
        return res;
    }

    /**
     * 获取当前季度的每月第一天和最后一天
     */
    List<Map<String, String>> getPerMonthLastDaysOfQuarter() {
        LocalDate today = LocalDate.now();
        Month month = today.getMonth();
        Month firstMonthOfQuarter = month.firstMonthOfQuarter();
        Month secondMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 1);
        Month thirdMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
        List<Map<String, String>> res = new ArrayList<>();
        Map<String, String> first = new HashMap<>();
        first.put("start", today.getYear() + "-" + (firstMonthOfQuarter.getValue() < 10 ? "0" + firstMonthOfQuarter.getValue() : firstMonthOfQuarter.getValue()) + "-01");
        first.put("end", today.getYear() + "-" + (firstMonthOfQuarter.getValue() < 10 ? "0" + firstMonthOfQuarter.getValue() : firstMonthOfQuarter.getValue()) + "-" + firstMonthOfQuarter.length(today.isLeapYear()));
        first.put("order", firstMonthOfQuarter.getValue() + "月");
        res.add(first);

        Map<String, String> second = new HashMap<>();
        second.put("start", today.getYear() + "-" + (secondMonthOfQuarter.getValue() < 10 ? "0" + secondMonthOfQuarter.getValue() : secondMonthOfQuarter.getValue()) + "-01");
        second.put("end", today.getYear() + "-" + (secondMonthOfQuarter.getValue() < 10 ? "0" + secondMonthOfQuarter.getValue() : secondMonthOfQuarter.getValue()) + "-" + secondMonthOfQuarter.length(today.isLeapYear()));
        second.put("order", secondMonthOfQuarter.getValue() + "月");
        res.add(second);

        Map<String, String> third = new HashMap<>();
        third.put("start", today.getYear() + "-" + (thirdMonthOfQuarter.getValue() < 10 ? "0" + thirdMonthOfQuarter.getValue() : thirdMonthOfQuarter.getValue()) + "-01");
        third.put("end", today.getYear() + "-" + (thirdMonthOfQuarter.getValue() < 10 ? "0" + thirdMonthOfQuarter.getValue() : thirdMonthOfQuarter.getValue()) + "-" + thirdMonthOfQuarter.length(today.isLeapYear()));
        third.put("order", thirdMonthOfQuarter.getValue() + "月");
        res.add(third);
        return res;
    }

    /**
     * 获取指定月的每周第一天和最后一天
     */
    List<Map<String, String>> getLastDaysOfMonthWeeks(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String timeStrs[] = date.split("-");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(timeStrs[0]));
        c.set(Calendar.MONTH, Integer.parseInt(timeStrs[1]) - 1);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        int weeks = c.getActualMaximum(Calendar.WEEK_OF_MONTH);

        LocalDate localDateate = LocalDate.parse(date, dateTimeFormatter);
        //月份第一周的起始时间和结束时间
        LocalDate firstDay = localDateate.with(TemporalAdjusters.firstDayOfMonth());
        String firstDayStr = firstDay.format(dateTimeFormatter);
        String sunStr = getSunOfWeek(firstDayStr);

        List<Map<String, String>> weekInfos = new ArrayList<>();
        for (int i = 1; i <= weeks; i++) {
            Map<String, String> weekInfo = new HashMap<>();
            //第一周的起始时间就是当月的1号，结束时间就是周日
            if (i == 1) {
                weekInfo.put("start", firstDayStr);
                weekInfo.put("end", sunStr);
                weekInfo.put("order", i + "");
                //计算接下来每周的周一和周日
            } else if (i < weeks) {
                //由于sunStr是上一周的周日，所以取周一要取sunStr的下一周的周一
                String monDay = getLastMonOfWeek(sunStr);
                sunStr = getSunOfWeek(monDay);
                weekInfo.put("start", monDay);
                weekInfo.put("end", sunStr);
                weekInfo.put("order", i + "");
                //由于最后一周可能结束时间不是周日，所以要单独处理
            } else {
                String monDay = getLastMonOfWeek(sunStr);
                //结束时间肯定就是当前月的最后一天
                LocalDate lastDay = localDateate.with(TemporalAdjusters.lastDayOfMonth());
                String endDay = lastDay.format(dateTimeFormatter);
                weekInfo.put("start", monDay);
                weekInfo.put("end", endDay);
                weekInfo.put("order", i + "");
            }

            weekInfos.add(weekInfo);
        }
        return weekInfos;
    }

    //算出所在周的周日
    public String getSunOfWeek(String time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDateate = LocalDate.parse(time, dateTimeFormatter);
        LocalDate endday = localDateate.with(TemporalAdjusters.next(java.time.DayOfWeek.MONDAY)).minusDays(1);
        String endDayStr = endday.format(dateTimeFormatter);
        return endDayStr;
    }

    //下一周的周一
    public String getLastMonOfWeek(String time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDateate = LocalDate.parse(time, dateTimeFormatter);
        LocalDate endday = localDateate.with(TemporalAdjusters.next(java.time.DayOfWeek.MONDAY));
        String endDayStr = endday.format(dateTimeFormatter);
        return endDayStr;
    }
}
