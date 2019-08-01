package com.imseeh.bradgeagent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView orders;
    List<Map<String, String>> saleList;
    Button search;
    EditText tablenumber;
    SimpleAdapter sAdap;
    TextView total;
    Double totalprice =0.0;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;
    LinearLayout list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        total = findViewById(R.id.totalPrice);
        orders = findViewById(R.id.sale_List);
        search = findViewById(R.id.search);
        list = findViewById(R.id.list);
        tablenumber = findViewById(R.id.tablenumber);
        saleList = new ArrayList<>();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tablenumber.getText().toString().equalsIgnoreCase("")&& tablenumber.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"يرجى تعبئة حقل رقم الطاولة",Toast.LENGTH_LONG).show();
                }
                else {
                saleList = getdata(tablenumber.getText().toString());
                sAdap = new SimpleAdapter(MainActivity.this, saleList,
                        R.layout.listview_lineitem, new String[]{"name","quantity","price"}, new int[] {R.id.name,R.id.quantity,R.id.price});
                orders.setAdapter(sAdap);

                for (int i =0 ; i<saleList.size();i++){
                    Double price = (Double.parseDouble(saleList.get(i).get("price")))*Double.parseDouble(saleList.get(i).get("quantity"));
                    totalprice += price;
                }
                total.setText(totalprice+" JD");
                list.setVisibility(View.VISIBLE);

                }
            }
        });

    }


    public List<Map<String, String>> getdata(String num) {
        List<Map<String, String>> data;
        data = new ArrayList<>();
        try
        {
            ConnectionClass conStr=new ConnectionClass();
            connect =conStr.CONN(MainActivity.this);
            if (connect == null)
            {
                ConnectionResult = "Check Your Internet Access!";
                return data;
            }
            else
            {

                String query = "select * from table"+num;
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()&&(!rs.wasNull())){
                    Map<String,String>map = new HashMap<>();
                    map.put("name",rs.getString("NAME"));
                    map.put("price",rs.getString("PRICE"));
                    map.put("quantity",rs.getString("quantity"));
                    data.add(map);
                }
                ConnectionResult = "successful";
                isSuccess=true;
                connect.close();
            }
        }
        catch (SQLException e){
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }
        catch (Exception ex)
        {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        System.out.println(ConnectionResult);
        return data;
    }
}