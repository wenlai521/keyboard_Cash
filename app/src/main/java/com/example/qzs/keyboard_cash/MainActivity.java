package com.example.qzs.keyboard_cash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //键盘
    private GridView gridview;
    private List<String> numList = new ArrayList<String>();
    private int numIndex=0;
    private String EditTxtValue="";



    private EditText editMemberAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setUpPayKeyAdapter();
    }

    private void setUpPayKeyAdapter() {
        final PayKeyAdapter adapter = new PayKeyAdapter(this);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String keyType = view.findViewById(R.id.GridTextView).getTag().toString();
                String textValue = ((TextView)view.findViewById(R.id.GridTextView)).getText().toString();
                String editText = editMemberAmount.getText().toString();


                if(keyType.equals("num")){
                    //数字键
                    try{
                        String orgValue = numList.get(numIndex);
                        if(editText.equals("0")){
                            editMemberAmount.setText(textValue);
                        }else{
                            //小数点最多只能后两位
                            if(orgValue.equals("0.00")){
                                orgValue=textValue;
                                numList.set(numIndex, textValue);
                                editMemberAmount.setText(textValue);
                                return;
                            }
                            if(orgValue.lastIndexOf(".")!=-1 && orgValue.lastIndexOf(".")<orgValue.length()-2){
                                Toast.makeText(getApplicationContext(),"只能输入小数点后两位",Toast.LENGTH_SHORT).show();
                            }else{
                                if(editText.length()>=18){
                                    Toast.makeText(getApplicationContext(),"一次输入算式无法超过18个字符",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                int numLen = orgValue.length();
                                if(orgValue.lastIndexOf(".")!=-1){
                                    numLen = orgValue.lastIndexOf(".")+1;
                                }
                                if(numLen==7){
                                    Toast.makeText(getApplicationContext(),"收款金额不能超过7位数",Toast.LENGTH_SHORT).show();
                                }else{
                                    numList.set(numIndex, orgValue+textValue);
                                    editMemberAmount.setText(editText+textValue);
                                }
                            }
                        }
                    }catch(Exception e){
                        numIndex=0;
                        numList=new ArrayList<String>();
                        numList.add(textValue);
                        editMemberAmount.setText(textValue);
                    }
                }
                if(keyType.equals("back")){
                    //退格
                    try{
                        if(editText.lastIndexOf("+") == editText.length()-1){
                            //最后为加则减少一位
                            numList.set(numIndex,"");
                            numIndex--;
                            editMemberAmount.setText(editText.substring(0,editText.length()-1));
                        }else{
                            String orgValue = numList.get(numIndex);
                            if(!orgValue.equals("")){
                                numList.set(numIndex, orgValue.substring(0,orgValue.length()-1));
                            }
                            if(editText.equals("0")||editText.length()==1){
                                editMemberAmount.setText("0.00");
                                numList.set(numIndex,"0.00");
                            }else{
                                editMemberAmount.setText(editText.substring(0,editText.length()-1));
                            }
                        }
                    }catch(Exception e){
                        numIndex=0;
                        numList=new ArrayList<String>();
                        numList.add("0");
                        editMemberAmount.setText("");
                    }
                }
                if(keyType.equals("point")){
                    //小数点
                    try{
                        String orgValue = numList.get(numIndex);
                        if(orgValue.lastIndexOf(".")!=-1){
                            Toast.makeText(getApplicationContext(),"已存在小数点",Toast.LENGTH_SHORT).show();
                        }else{
                            if(editText.lastIndexOf("+") == editText.length()-1){
                                //最后为加则补0
                                numList.set(numIndex, orgValue+"0"+textValue);
                                editMemberAmount.setText(editText+"0"+textValue);
                            }else{
                                numList.set(numIndex, orgValue+textValue);
                                editMemberAmount.setText(editText+textValue);
                            }
                        }
                    }catch(Exception e){
                        numIndex=0;
                        numList=new ArrayList<String>();
                        numList.add("0.");
                        editMemberAmount.setText("0.");
                    }
                }
                if(keyType.equals("plus")){
                    //加
                    try{
                        if(editText.lastIndexOf("+") == editText.length()-1){
                            //最后为加则不处理

                        }else{
                            numIndex++;
                            numList.add("");
                            editMemberAmount.setText(editText+textValue);
                        }
                    }catch(Exception e){
                        numIndex=0;
                        numList=new ArrayList<String>();
                        numList.add("0");
                        editMemberAmount.setText("");
                    }
                }
                if(keyType.equals("cancel")){
                    //清空
                    //确认是否清空
                    AlertDialog.Builder clearBuilder = new AlertDialog.Builder(MainActivity.this);// 添加按钮的单击事件
                    clearBuilder.setMessage("是否确认要清空当前收款数据?")
                            . // 设置确定按钮
                            setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                // 单击事件
                                public void onClick(
                                        DialogInterface dialog,
                                        int which) { // 设置TextView文本
                                    clearInputData();
                                }
                            })
                            . // 设置取消按钮
                            setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int which) {

                                }
                            }); // 创建对话框
                    AlertDialog ad = clearBuilder.create(); // 显示对话框
                    ad.show();
                }
                if(keyType.equals("equal")||keyType.equals("pay")){
                    //等于
                    try{
                        double sum = 0.0;
                        for(String value :numList){
                            if(!value.equals("")){
                                sum+= Double.parseDouble(value)+0.00d;
                            }
                        }

                        String sumStr=new DecimalFormat("##.##").format(sum);
                        if(!sumStr.contains(".")){
                            sumStr+=".00";
                        }
                        if(sumStr.substring(sumStr.lastIndexOf(".")+1).length()<2){
                            sumStr+="0";
                        }

                        numIndex=0;
                        numList=new ArrayList<String>();
                        numList.add(sumStr);
                        editMemberAmount.setText(sumStr);
                    }catch(Exception e){
                        numIndex=0;
                        numList=new ArrayList<String>();
                        numList.add("0");
                        editMemberAmount.setText("");
                    }
                }
                if(keyType.equals("pay")){
                    //收款
                    if (editMemberAmount.getText().toString().length() < 1||editMemberAmount.getText().toString().equals("0.00") || editMemberAmount.getText().toString().equals("0")) {
                        Toast.makeText(getApplicationContext(),"请输入金额",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        Toast.makeText(getApplicationContext(),"收款成功",Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });


    }

    private void initView() {
        editMemberAmount= (EditText) findViewById(R.id.editMemberAmount);
        editMemberAmount.setInputType(InputType.TYPE_NULL);
        gridview= (GridView) findViewById(R.id.gridview);
    }
    private void clearInputData(){
        numIndex=0;
        numList=new ArrayList<String>();
        editMemberAmount.setText("");
        EditTxtValue="0";

    }

}
