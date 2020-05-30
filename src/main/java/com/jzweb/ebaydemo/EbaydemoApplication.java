package com.jzweb.ebaydemo;

import com.ebay.api.client.auth.oauth2.OAuth2Api;
import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.call.*;
import com.ebay.sdk.eBayAccount;
import com.ebay.soap.eBLBaseComponents.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Calendar;

@SpringBootApplication
public class EbaydemoApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EbaydemoApplication.class, args);

        ApiContext apiContext = getApiContext();

        System.out.println("==== [2] Item信息  ====");
        ItemType item = buildItem();

        // [Step 3] 创建调用对象并执行调用
//        System.out.println("==== [3] 执行API调用  ====");
//        System.out.println("开始调用eBay API，请稍候…  ");
//        AddItemCall api = new AddItemCall(apiContext);
//        api.setItem(item);
//        FeesType fees = api.addItem();
//        System.out.println("调用eBay API结束，显示调用结果…"+fees.toString());

        GetCategoriesCall api = new GetCategoriesCall(apiContext);
        CategoryType[] typs = api.getCategories();
        System.out.println("--------------Categories---------------");
        System.out.println(typs);
//
        GetOrdersCall ordersCall = new GetOrdersCall(apiContext);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DATE,-100);
        ordersCall.setCreateTimeFrom(calendar1);
        ordersCall.setCreateTimeTo(Calendar.getInstance());
        PaginationType pagination = new PaginationType();
        pagination.setPageNumber(1);
        pagination.setEntriesPerPage(200);
        ordersCall.setPagination(pagination);
        OrderType[] orders = ordersCall.getOrders();
        System.out.println("---------------order--------------"+orders.length);
        if(orders != null){
            for(OrderType orderType : orders){
                System.out.println(orderType.toString());
            }
        }
//
//        GetItemCall itemCall = new GetItemCall(apiContext);
//        itemCall.setItemID("110518085407");
//        ItemType itemType = itemCall.getItem();
//        System.out.println("---------------------------");
//        System.out.println(itemType.toString());

        //getSeesionID();
    }

    public static ApiContext getApiContext(){
        ApiContext apiContext = new ApiContext();
        ApiCredential cred = apiContext.getApiCredential();
        cred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**/ObRXg**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFk4aiC5SBoAqdj6x9nY+seQ**g0cFAA**AAMAAA**dLprZcq2thGkKKfJXXiW3sHsPznCfnqLyLIEM0daehT1J01NSc9xTKPbWLeMZ9AeFUI1VONn+AVe7AuE44QlVps1l/rk2S3XTm52MCqbvpKhMx+TU9iuLdvbGIDnN+WCXhcuCYQBX2yXQgMo7XSZD7fTyX1m+VstCw+In+MD4G4qwuznv2jLR05NGsCFKxflZGbVVdsuNoc8BcdP32w8rpLtnPC5JHsmfGuUVNjUF9yYO6QYpWisjvnnQC1JWprvmCs9b26M5WMgbATwAyHl34gGu09b0i6mNYTY2yExV+wngL8kXWm+ULFpp8+OkmNGVnVmEa089uC/a21BJw85SsKbz957K57kml24H5F1LyfWOyUHG9OndfpEak59EBS1uYHHYcL5MHYFlIQjwC1iCAutk24sOFmy6ewJhWrT09568wc+mJYBBQv93Uk/YAWcmLqTcEgq0e00ecfaWApLvdEXajmKoocdapoUah1jfFqDgditLlI6tyfdMfUqrC8vaglxDZcRCz0yaC449K+5UxtmtqqUylyntzmbVZ+R9/SOIMnYfPRC59dURGnbeX+JvRAgtkb4qxcbhrATFzgw/gE+If72L0vEoidY1qfNx4bj3LsP4tp6jH3rMS4ZHDpDI91fkU+BvISwSkAeIKjQCONxJ/P0WC1WY8se7s739SBDVM0HPZ5hby5wi2Z5r+WGifnM8fz/No9TmS/xj/YIpYcHo2aAZSsikxMf1vH6xdK8SgqoAPig7OAGbRtz4BVW");

        apiContext.setApiServerUrl("https://api.sandbox.ebay.com/wsapi");
        return apiContext;
    }

    public static String getSeesionID() throws Exception {
        ApiAccount apiAccount = new ApiAccount();
        apiAccount.setApplication("-getOrder-SBX-d2eae7200-ed275192");
        apiAccount.setDeveloper("60aaab77-3d12-4e75-b766-117d01e32974");
        apiAccount.setCertificate("SBX-2eae720081ce-1656-41b3-b6e2-4c3e");

        ApiContext apiContext = new ApiContext();
        apiContext.getApiCredential().setApiAccount(apiAccount);
        apiContext.setApiServerUrl("https://api.sandbox.ebay.com/wsapi");
        apiContext.setErrorLanguage("zh_CN");

        GetSessionIDCall call = new GetSessionIDCall(apiContext);
        call.setRuName("_--getOrder-SBX-d-ilvhz");

        String sessionID = call.getSessionID();
        System.out.println("sessionID=="+sessionID);

        FetchTokenCall tokenCall = new FetchTokenCall(apiContext);
        tokenCall.setSessionID(sessionID);
        try {
            String token = tokenCall.fetchToken();
            System.out.println("=============================================");
            System.out.println(token);
            System.out.println("************************************************");
        }catch (Exception e){
            e.printStackTrace();
        }
        //"https://signin.sandbox.ebay.com/ws/eBayISAPI.dll?SignIn&" + "RuName=" + ruName+ "&SessID=" + sessionID;
        return sessionID;
    }

    public void getUserTokenBySessionID(){
        ApiContext apiContext = new ApiContext();
    }


    public static ItemType buildItem(){
        ItemType item = new ItemType();
        item.setTitle("HightCard");
        String[] paths = {"http://www.dilianidc.com/templets/twang/images/tw_11.jpg","http://www.dilianidc.com/templets/twang/images/tw_20.jpg"};
        PictureDetailsType pic = new PictureDetailsType();
        pic.setPictureURL(paths);
        pic.setGalleryType(GalleryTypeCodeType.GALLERY);
        item.setPictureDetails(pic);
        item.setSKU("");
        item.setDescription("It's a Card!");
        item.setListingType(ListingTypeCodeType.CHINESE);

        item.setCurrency(CurrencyCodeType.USD);
        AmountType amount = new AmountType();
        amount.setValue(Double.valueOf("0.01"));
        //item.setStartPrice(amount);
        item.setCeilingPrice(amount);
        item.setListingDuration(ListingDurationCodeType.DAYS_1.value());

        item.setLocation("CN");
        item.setCountry(CountryCodeType.US);
        CategoryType cat = new CategoryType();
        cat.setCategoryID("30022");
        item.setPrimaryCategory(cat);
        item.setQuantity(new Integer(1));
        item.setPaymentMethods(new BuyerPaymentMethodCodeType[]{BuyerPaymentMethodCodeType.PAY_PAL});
        item.setPayPalEmailAddress("88566207@qq.com");
        item.setConditionID(1000);
        item.setDispatchTimeMax(Integer.valueOf(1));
        item.setShippingDetails(buildShippingDetails());
        ReturnPolicyType returnPolicy = new ReturnPolicyType();
        returnPolicy.setReturnsAcceptedOption("ReturnsAccepted");
        item.setReturnPolicy(returnPolicy);
        return item;
    }

    /**
     * 构建产品运输细节
     * @return ShippingDetailsType 对象
     */
    private static ShippingDetailsType buildShippingDetails() {

        // 运输细节
        ShippingDetailsType sd =new ShippingDetailsType();

        sd.setApplyShippingDiscount(new Boolean(true));
        AmountType amount =new AmountType();
        amount.setValue(2.8);
        sd.setPaymentInstructions("eBay Java SDK 测试指令.");

        // 选择航运类型和航运服务
        sd.setShippingType(ShippingTypeCodeType.FLAT);
        ShippingServiceOptionsType shippingOptions  = new ShippingServiceOptionsType();
        shippingOptions.setShippingService(ShippingServiceCodeType.SHIPPING_METHOD_STANDARD.value());

        //amount = new AmountType();
        amount.setValue(2.0);
        shippingOptions.setShippingServiceAdditionalCost(amount);

        //amount  = new AmountType();
        amount.setValue(10);
        shippingOptions.setShippingServiceCost(amount);
        shippingOptions.setShippingServicePriority(new Integer(1));

        //amount  = new AmountType();
        amount.setValue(1);
        shippingOptions.setShippingInsuranceCost(amount);

        sd.setShippingServiceOptions(new ShippingServiceOptionsType[]{shippingOptions});

        return sd;
    }

}
