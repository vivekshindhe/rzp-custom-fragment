package com.vivek.rzpcustomfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.razorpay.PaymentResultListener;
import com.razorpay.Razorpay;
import com.razorpay.ValidationListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentFragment extends Fragment implements PaymentResultListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View root;
    private Activity activity;
    private Button payBtn;
    private WebView webView;
    private Razorpay razorpay;
    private JSONObject payload;

    public PaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentFragment newInstance(String param1, String param2) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.root = inflater.inflate(R.layout.fragment_payment, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        payBtn = root.findViewById(R.id.pay_btn);
        webView = root.findViewById(R.id.webview);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    payload = new JSONObject("{currency: 'INR'}");
                    payload.put("amount", "111");
                    payload.put("contact", "9999999999");
                    payload.put("email", "customer@name.com");
//                                    payload.put("upi_app_package_name", app.getPackageName());
                    payload.put("display_logo", true);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray jArray = new JSONArray();
                    jArray.put("in.org.npci.upiapp");
                    jArray.put("com.snapwork.hdfc");
                    payload.put("description","Credits towards consultation");
                    payload.put("method", "upi");
                    payload.put("_[flow]", "intent");
                    payload.put("preferred_apps_order", jArray);
                    payload.put("other_apps_order", jArray);
                    sendRequest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initialize(Activity activity){
        this.activity = activity;
        razorpay = new Razorpay(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        razorpay.setWebView(webView);
    }

    private void sendRequest(){
        razorpay.validateFields(payload, new ValidationListener() {
            @Override
            public void onValidationSuccess() {
                try {
                    webView.setVisibility(View.VISIBLE);
                    payBtn.setVisibility(View.GONE);
                    razorpay.submit(payload,PaymentFragment.this);
                } catch (Exception e) {
                    Log.e("com.example", "Exception: ", e);
                }
            }

            @Override
            public void onValidationError(Map error) {
                String s = error.get("field").toString();
                Toast.makeText(root.getContext(),s,Toast.LENGTH_LONG);
            }
        });
    }


    @Override
    public void onPaymentSuccess(String s) {
        webView.setVisibility(View.GONE);
        payBtn.setVisibility(View.VISIBLE);
        Toast.makeText(root.getContext(),"SUCCESS::"+s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        webView.setVisibility(View.GONE);
        payBtn.setVisibility(View.VISIBLE);
        Toast.makeText(root.getContext(),"FAILED::"+s,Toast.LENGTH_LONG).show();

    }

    public void onActivityResultFrag(int requestCode, int resultCode, Intent data) {
        razorpay.onActivityResult(requestCode,resultCode,data);
    }

    public void onBackPressed(){
        webView.setVisibility(View.GONE);
        payBtn.setVisibility(View.VISIBLE);
    }
}