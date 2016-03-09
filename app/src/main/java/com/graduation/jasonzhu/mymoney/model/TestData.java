package com.graduation.jasonzhu.mymoney.model;

import com.graduation.jasonzhu.mymoney.adapter.DayAccountExpandLvAdapter;
import com.graduation.jasonzhu.mymoney.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gemha on 2016/2/17.
 */
public class TestData {

    private static List<Account> accounts;
    private static List<String> mainCategory;
    private static List<List<String>> subCategory;
    private static List<String> expandListGroup;
    private static List<List<IncomeAndExpense>> incomeAndExpenseList;
    private static List<Category> categoryList;
    private static List<String> categoryTypes;

    public static List<String> getCategoryTypes() {
        categoryTypes = new ArrayList<>();
        categoryTypes.add("一级类别");
        categoryTypes.add("二级类别");
        return categoryTypes;
    }

    public static List<Category> getCategoryList() {
        categoryList = new ArrayList<>();
        String time = TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
        List<Category> categoryChildList1 = new ArrayList<>();
        categoryChildList1.add(new Category("支出","早餐",time));
        categoryChildList1.add(new Category("支出","午餐",time));
        categoryChildList1.add(new Category("支出","晚餐",time));

        List<Category> categoryChildList2 = new ArrayList<>();
        categoryChildList2.add(new Category("支出","鞋子",time));
        categoryChildList2.add(new Category("支出","衣服",time));
        categoryChildList2.add(new Category("支出","裤子",time));
        categoryChildList2.add(new Category("支出","袜子",time));

        List<Category> categoryChildList3 = new ArrayList<>();
        categoryChildList3.add(new Category("支出","书籍",time));
        categoryChildList3.add(new Category("支出","学习用具",time));
        categoryChildList3.add(new Category("支出","学费",time));

        List<Category> categoryChildList4 = new ArrayList<>();
        categoryChildList4.add(new Category("支出","水费",time));
        categoryChildList4.add(new Category("支出","电费",time));
        categoryChildList4.add(new Category("支出","煤气费",time));
        categoryChildList4.add(new Category("支出","房租",time));

        List<Category> categoryChildList5 = new ArrayList<>();
        categoryChildList5.add(new Category("支出","公共交通",time));
        categoryChildList5.add(new Category("支出","打车",time));
        categoryChildList5.add(new Category("支出","私家车",time));

        List<Category> categoryChildList6 = new ArrayList<>();
        categoryChildList6.add(new Category("支出","运动健身",time));
        categoryChildList6.add(new Category("支出","腐败聚会",time));
        categoryChildList6.add(new Category("支出","旅游",time));

        List<Category> categoryChildList7 = new ArrayList<>();
        categoryChildList7.add(new Category("收入","工资",time));
        categoryChildList7.add(new Category("收入","奖金",time));
        categoryChildList7.add(new Category("收入","投资",time));
        categoryChildList7.add(new Category("收入","兼职",time));

        List<Category> categoryChildList8 = new ArrayList<>();
        categoryChildList8.add(new Category("收入","中奖",time));
        categoryChildList8.add(new Category("收入","意外之财",time));
        categoryChildList8.add(new Category("收入","红包",time));

        Category category1 = new Category("支出","餐饮",time,categoryChildList1);
        Category category2 = new Category("支出","购物",time,categoryChildList2);
        Category category3 = new Category("支出","学习",time,categoryChildList3);
        Category category4 = new Category("支出","住房",time,categoryChildList4);
        Category category5= new Category("支出","行车交通",time,categoryChildList5);
        Category category6= new Category("支出","休闲娱乐",time,categoryChildList6);
        Category category7 = new Category("收入","职业收入",time,categoryChildList7);
        Category category8 = new Category("收入","其他收入",time,categoryChildList8);

        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);
        categoryList.add(category4);
        categoryList.add(category5);
        categoryList.add(category6);
        categoryList.add(category7);
        categoryList.add(category8);
        return categoryList;
    }

    public static List<String> getExpandListGroup(){
        expandListGroup = new ArrayList<>();
        expandListGroup.add("2月");
        expandListGroup.add("1月");
        return expandListGroup;
    }

    public static List<List<IncomeAndExpense>> getIncomeAndExpenseList(){
        incomeAndExpenseList = new ArrayList<>();
        List<IncomeAndExpense> incomeAndExpenseList1 = new ArrayList<>();
        List<IncomeAndExpense> incomeAndExpenseList2 = new ArrayList<>();
        List<Category> categorySet1= new ArrayList<>();
        List<Category> categorySet2= new ArrayList<>();
        categorySet1.add(new Category("支出","书籍","2015-02-19 16:37"));
        categorySet2.add(new Category("收入", "工资", "2015-02-19 16:37"));
        IncomeAndExpense incomeAndExpense1 = new IncomeAndExpense("支出",200.00f,"2015-01-19 16:37",new Account("现金",2000.00f)
                ,new Category("支出","学习","2015-02-19 16:37",categorySet1));
        IncomeAndExpense incomeAndExpense2 = new IncomeAndExpense("支出",200.00f,"2015-01-26 16:37",new Account("支付宝",2000.00f)
                ,new Category("支出","学习","2015-02-19 16:37",categorySet1));
        IncomeAndExpense incomeAndExpense3 = new IncomeAndExpense("支出",200.00f,"2015-02-19 16:37",new Account("储蓄卡",2000.00f)
                ,new Category("支出","学习","2015-02-19 16:37",categorySet2));
        IncomeAndExpense incomeAndExpense4 = new IncomeAndExpense("支出",200.00f,"2015-02-22 16:37",new Account("现金",2000.00f)
                ,new Category("支出","学习","2015-02-19 16:37",categorySet2));

        IncomeAndExpense incomeAndExpense5 = new IncomeAndExpense("收入",200.00f,"2015-01-19 16:37",new Account("储蓄卡",2000.00f)
                ,new Category("收入","职业","2015-02-19 16:37",categorySet1));
        IncomeAndExpense incomeAndExpense6 = new IncomeAndExpense("收入",200.00f,"2015-01-19 16:37",new Account("储蓄卡",2000.00f)
                ,new Category("收入","职业","2015-02-19 16:37",categorySet1));
        IncomeAndExpense incomeAndExpense7 = new IncomeAndExpense("收入",200.00f,"2015-02-19 16:37",new Account("储蓄卡",2000.00f)
                ,new Category("收入","职业","2015-02-19 16:37",categorySet2));
        IncomeAndExpense incomeAndExpense8 = new IncomeAndExpense("收入",200.00f,"2015-02-22 16:37",new Account("储蓄卡",2000.00f)
                ,new Category("收入","职业","2015-02-19 16:37",categorySet2));

        //1月明细
        incomeAndExpenseList1.add(incomeAndExpense2);
        incomeAndExpenseList1.add(incomeAndExpense6);
        incomeAndExpenseList1.add(incomeAndExpense5);
        incomeAndExpenseList1.add(incomeAndExpense1);

        //2月明细
        incomeAndExpenseList2.add(incomeAndExpense8);
        incomeAndExpenseList2.add(incomeAndExpense4);
        incomeAndExpenseList2.add(incomeAndExpense7);
        incomeAndExpenseList2.add(incomeAndExpense3);

        incomeAndExpenseList.add(incomeAndExpenseList2);
        incomeAndExpenseList.add(incomeAndExpenseList1);
//        incomeAndExpenseList1.add(incomeAndExpense1);
//        incomeAndExpenseList1.add(incomeAndExpense5);
//        incomeAndExpenseList1.add(incomeAndExpense6);
//        incomeAndExpenseList1.add(incomeAndExpense2);
//        incomeAndExpenseList1.add(incomeAndExpense3);
//        incomeAndExpenseList1.add(incomeAndExpense7);
//        incomeAndExpenseList1.add(incomeAndExpense4);
//        incomeAndExpenseList1.add(incomeAndExpense8);

        return incomeAndExpenseList;
    }

    public static List<Account> getAccounts(){
        accounts = new ArrayList<Account>();
        Account account = new Account("现金",2999.00f);
        Account account2 = new Account("支付宝",2222.62f);
        Account account3 = new Account("信用卡",999999.99f);
        accounts.add(account);
        accounts.add(account2);
        accounts.add(account3);
        return accounts;
    }

    public static List<String> getMainCategory(){
        mainCategory = new ArrayList<String>();
        mainCategory.add("娱乐");
        mainCategory.add("购物");
        mainCategory.add("美食");
        mainCategory.add("旅游");
        mainCategory.add("学习");
        return mainCategory;
    }

    public static List<List<String>> getSubCategory() {
        subCategory = new ArrayList<List<String>>();
        List<String> list1  = new ArrayList<String>();
        List<String> list2  = new ArrayList<String>();
        List<String> list3  = new ArrayList<String>();
        List<String> list4  = new ArrayList<String>();
        List<String> list5  = new ArrayList<String>();
        list1.add("电影1");
        list1.add("电影2");
        list1.add("电影3");
        list1.add("电影4");
        list1.add("电影5");
        list1.add("电影6");
        list1.add("电影7");
        list1.add("电影8");
        list1.add("电影9");
        list1.add("电影10");
        list1.add("电影11");
        list1.add("电影12");
        list1.add("电影13");
        list1.add("电影14");
        list1.add("电影15");

        list2.add("衣服1");
        list2.add("衣服2");
        list2.add("衣服3");
        list2.add("衣服4");
        list2.add("衣服5");
        list2.add("衣服6");
        list2.add("衣服7");
        list2.add("衣服8");
        list2.add("衣服9");
        list2.add("衣服10");
        list2.add("衣服11");
        list2.add("衣服12");
        list2.add("衣服13");
        list2.add("衣服14");

        list3.add("炸鸡1");
        list3.add("炸鸡2");
        list3.add("炸鸡3");
        list3.add("炸鸡4");
        list3.add("炸鸡5");
        list3.add("炸鸡6");
        list3.add("炸鸡7");
        list3.add("炸鸡8");
        list3.add("炸鸡9");
        list3.add("炸鸡10");
        list3.add("炸鸡11");
        list3.add("炸鸡12");
        list3.add("炸鸡13");
        list3.add("炸鸡14");
        list3.add("炸鸡15");
        list3.add("炸鸡16");

        list4.add("火星1");
        list4.add("火星2");
        list4.add("火星3");
        list4.add("火星4");
        list4.add("火星5");
        list4.add("火星6");
        list4.add("火星7");
        list4.add("火星8");
        list4.add("火星9");
        list4.add("火星10");
        list4.add("火星11");
        list4.add("火星12");
        list4.add("火星13");
        list4.add("火星14");
        list4.add("火星15");
        list4.add("火星16");

        list5.add("书籍1");
        list5.add("书籍2");
        list5.add("书籍3");
        list5.add("书籍4");
        list5.add("书籍5");
        list5.add("书籍6");
        list5.add("书籍7");
        list5.add("书籍8");
        list5.add("书籍9");
        list5.add("书籍10");
        list5.add("书籍11");
        list5.add("书籍12");
        list5.add("书籍13");
        list5.add("书籍14");
        list5.add("书籍15");
        list5.add("书籍16");

        subCategory.add(list1);
        subCategory.add(list2);
        subCategory.add(list3);
        subCategory.add(list4);
        subCategory.add(list5);
        return subCategory;
    }
}
