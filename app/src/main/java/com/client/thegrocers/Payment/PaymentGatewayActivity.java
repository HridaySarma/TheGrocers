package com.client.thegrocers.Payment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.atom.mpsdklibrary.PayActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.yuvraj.thegroceryapp.Common.Common;
import com.yuvraj.thegroceryapp.Database.CartItem;
import com.yuvraj.thegroceryapp.EventBus.OnlinePaymentSuccessFull;
import com.yuvraj.thegroceryapp.Model.PaymentCredentials;
import com.yuvraj.thegroceryapp.R;
import com.yuvraj.thegroceryapp.home.HomeActivity;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import dmax.dialog.SpotsDialog;

public class PaymentGatewayActivity extends AppCompatActivity {

    private String amt;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private Date date;
    String dateVerified;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Common.PaymentGatewayOrderDetails.getCreateDate());
        date = new Date(Common.PaymentGatewayOrderDetails.getCreateDate());
        alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(PaymentGatewayActivity.this).build();
        alertDialog.setMessage("Starting Payment Gateway");
        alertDialog.show();
        String dateOfOrder = simpleDateFormat.format(date);
        dateVerified = dateOfOrder.replace("-","/");

            FirebaseDatabase.getInstance().getReference("PaymentCredential")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                PaymentCredentials paymentCredentials = snapshot.getValue(PaymentCredentials.class);
                                if (paymentCredentials != null){
                                    try {
                                        alertDialog.dismiss();
                                        startPayment(paymentCredentials);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(PaymentGatewayActivity.this, "Transaction failed ! Please check your internet connection", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(PaymentGatewayActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
    }
    private void startPayment(PaymentCredentials paymentCredentials) throws Exception {
        Random random = new Random();
        int newRando = random.nextInt(999);
        int txnid = random.nextInt(999999);
        Intent newPayIntent = new Intent(PaymentGatewayActivity.this, PayActivity.class);
        newPayIntent.putExtra("merchantId", paymentCredentials.getLoginId());
        newPayIntent.putExtra("txnscamt", "0");
        newPayIntent.putExtra("loginid", paymentCredentials.getLoginId());
        newPayIntent.putExtra("password", paymentCredentials.getPassword());
        newPayIntent.putExtra("prodid", "YUVRAJ");
        newPayIntent.putExtra("txncurr", "INR");
        newPayIntent.putExtra("clientcode", encodeBase64("007"));
        newPayIntent.putExtra("custacc", Common.currentUser.getPhone()+String.valueOf(newRando));
        newPayIntent.putExtra("channelid", "INT");
        newPayIntent.putExtra("amt", Common.PaymentGatewayOrderDetails.getFinalPayment()+"0");
        newPayIntent.putExtra("txnid", String.valueOf(txnid));

        newPayIntent.putExtra("date", dateVerified);
        newPayIntent.putExtra("signature_request", "a210a5843fa3542964");
        newPayIntent.putExtra("signature_response", "ae38fae97cd03f43c3");
        newPayIntent.putExtra("discriminator", "All");
        newPayIntent.putExtra("isLive", true);
//Optinal Parameters
//Only for Name
        newPayIntent.putExtra("customerName", Common.PaymentGatewayOrderDetails.getUserName());
//Only for Email ID//Only for Mobile Number
        newPayIntent.putExtra("customerMobileNo", Common.PaymentGatewayOrderDetails.getUserPhone());
//Only for Address
        newPayIntent.putExtra("billingAddress", Common.PaymentGatewayOrderDetails.getAddress().getAddress());
// Can pass any data
//        newPayIntent.putExtra("optionalUdf9", "OPTIONAL  2");
// Pass data in XML format, only for Multi product
        newPayIntent.putExtra("mprod", createXmlForProducts());

        startActivityForResult(newPayIntent, 99);
    }

    public String encodeBase64(String encode) {
        String decode = null;

        try {
            decode = Base64.encode(encode.getBytes());
        } catch (Exception e) {
            System.out.println("Unable to decode : " + e);
        }
        return decode;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 )
        {

            System.out.println("---------INSIDE-------");
            if (data != null)
            {
                String message = data.getStringExtra("status");
                String[] resKey = data.getStringArrayExtra("responseKeyArray");
                String[] resValue = data.getStringArrayExtra("responseValueArray");
                if(resKey!=null && resValue!=null)
                {
                    for(int i=0; i<resKey.length; i++)
                        System.out.println("  "+i+" resKey : "+resKey[i]+" resValue : "+resValue[i]);
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                if (message.toUpperCase().contains("SUCCESS")){
                    EventBus.getDefault().postSticky(new OnlinePaymentSuccessFull(true,Common.PaymentGatewayOrderDetails));
                    startActivity(new Intent(PaymentGatewayActivity.this, HomeActivity.class));
                    finish();
                }
                System.out.println("RECEIVED BACK--->" + message);

            }
        }


    }

    private String createXmlForProducts() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document testDoc = builder.newDocument();

        ArrayList<String> lst = new ArrayList<String>();
        for (CartItem cartItem : Common.PaymentGatewayOrderDetails.getCartItemList()){
            lst.add(""+ cartItem.getProductId() +","+cartItem.getProductName()+","+cartItem.getProductSellingPrice()+","+cartItem.getProductQuantity());
        }

        int doubleAmt = 0;
        Element products = testDoc.createElement("products");
        testDoc.appendChild(products);
        for (String s : lst) {
            String line[] = s.split(",");

            Element product = testDoc.createElement("product");
            products.appendChild(product);
            Element id = testDoc.createElement("id");
            id.appendChild(testDoc.createTextNode(line[0]));
            product.appendChild(id);
            Element name = testDoc.createElement("name");
            name.appendChild(testDoc.createTextNode(line[1]));
            product.appendChild(name);

            Element amount = testDoc.createElement("amount");
            amount.appendChild(testDoc.createTextNode(line[2]));
            product.appendChild(amount);
            doubleAmt = doubleAmt + (int) Double.parseDouble(line[2]);
            amt = Integer.toString(doubleAmt);

            if (line.length > 3) {
                Element param1 = testDoc.createElement("param1");
                param1.appendChild(testDoc.createTextNode(line[3]));
                product.appendChild(param1);
            }

            if (line.length > 4) {

                Element param2 = testDoc.createElement("param2");
                param2.appendChild(testDoc.createTextNode(line[4]));
                product.appendChild(param2);

            }

            if (line.length > 5) {

                Element param3 = testDoc.createElement("param3");
                param3.appendChild(testDoc.createTextNode(line[5]));
                product.appendChild(param3);

            }

            if (line.length > 6) {
                Element param4 = testDoc.createElement("param4");
                param4.appendChild(testDoc.createTextNode(line[6]));
                product.appendChild(param4);

            }


            if (line.length > 7) {
                Element param5 = testDoc.createElement("param5");
                param5.appendChild(testDoc.createTextNode(line[7]));
                product.appendChild(param5);

            }

        }

        System.out.println("Total Amount :::" + amt);
        try {
            DOMSource source = new DOMSource(testDoc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(source, result);
            writer.flush();

            String s = writer.toString().split("\\?")[2].substring(1, writer.toString().split("\\?")[2].length());
            System.out.println("Product XML : " + s);
            return s;
        } catch (TransformerException ex) {
            ex.printStackTrace();
            return null;
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}