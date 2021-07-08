package com.client.thegrocers.Common;

import com.client.thegrocers.Model.AddressModel;
import com.client.thegrocers.Model.BuyNowClass;
import com.client.thegrocers.Model.CategoryModel;
import com.client.thegrocers.Model.Order;
import com.client.thegrocers.Model.ProductModel;
import com.client.thegrocers.Model.UserModel;
import com.client.thegrocers.UpdatedPackages.CommonNew.NewUserMode;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class Common {
    public static final String CATEGORIES_REF = "Categories";
    public static final String ORDERS_REF = "Orders";
    public static final String BANNERS_REF = "HomePager";
    public static final String USER_REFERENCE = "Users";
    public static final String ONBOARD_SAVE = "OnboardingSave";
    public static final String LOGINSTATUS = "LoginStatus";
    public static final String ONBOARDING_REF = "Onboarding";
    public static final String POPULAR_CATEGORIES_REF = "PopularCategories";
    public static final String BEST_DEALS_REF = "BestDealsRef";
    public static final String ORDER_REF = "Orders";
    public static final String COUPON_CODES_REF = "Coupons";
    public static final String ALL_PRODUCTS_REF = "AllProducts";
    public static final String NEW_USERS_REF = "NewUsers";
    public static final String ONGOING_ORDERS_REF = "OngoingOrdersRef";
    public static  boolean SIGNINFROMACCOUNT ;
    public static CategoryModel categorySelected;
    public static UserModel currentUser;
    public static BuyNowClass buyNowClass;
    public static ProductModel selectedProduct;
    public static String mapboxKey;
    public static AddressModel AddressToBeSelected;
    public static Order PaymentGatewayOrderDetails;
    public static AddressModel addressSelectedForDelivery;
    public static Order orderPlacedViaCod;
    public static String CurrentFragment;
    public static Order orderSelectedForDetails;
    public static NewUserMode newCurrentUser;

    public static String formatPrice(double displayprice) {
        if (displayprice !=0){
            DecimalFormat df = new DecimalFormat("#,##0.00");
            df.setRoundingMode(RoundingMode.UP);
            String finalPrice = new StringBuilder(df.format(displayprice)).toString();
            return finalPrice;
        }else {
            return "0.00";
        }
    }

    public static String createOrderNumber() {

        return new StringBuilder().append(System.currentTimeMillis())
                .append(Math.abs(new Random().nextInt()))
                .toString();
    }

    public static String convertOrderStatusToText(int orderStatus) {
        switch (orderStatus){
            case 0:
                return "Placed";
            case 1:
                return "Shipping";
            case 2:
                return "Delivered";
            case 3:
                return "Delivery On Process";
            case 4:
                return "Requested Return";
            case 5:
                return "Return Approved";
            case 6:
                return "Returned";
            case 7:
                return "Out of stock";
            case -1:
                return "Cancelled";
            default:return "Unk";
        }
    }

    public static String getDateOfWeek(int i) {
        switch (i){
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:return "unk";
        }
    }

}
