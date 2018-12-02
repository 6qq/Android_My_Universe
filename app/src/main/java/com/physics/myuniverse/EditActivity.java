package com.physics.myuniverse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;

import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<EditText>editTexts = new ArrayList<>();
    ArrayAdapter<CharSequence>specialPlanet;
    ArrayAdapter<CharSequence>specialStar;
    ArrayAdapter<CharSequence>specialBlackhole;
    ArrayAdapter<CharSequence>specialBurst;
    ArrayAdapter<CharSequence>specialStruct;
    Spinner specialSpinner;
    TableLayout editTable;
    DecimalFormat format = new DecimalFormat("0.##########");
    String type, createType = "planet", specialType = "Earth";
    Body preparedBody;
    Boolean cleanMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        specialSpinner = new Spinner(this);
        specialPlanet =  ArrayAdapter.createFromResource(this,R.array.special_type_planet,android.R.layout.simple_spinner_item);
        specialPlanet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialStar =  ArrayAdapter.createFromResource(this,R.array.special_type_star,android.R.layout.simple_spinner_item);
        specialStar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialBlackhole =  ArrayAdapter.createFromResource(this,R.array.special_type_blackhole,android.R.layout.simple_spinner_item);
        specialBlackhole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialBurst =  ArrayAdapter.createFromResource(this,R.array.special_type_burst,android.R.layout.simple_spinner_item);
        specialBurst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialStruct =  ArrayAdapter.createFromResource(this,R.array.special_type_struct,android.R.layout.simple_spinner_item);
        specialStruct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Intent i = getIntent();
        type = i.getStringExtra("type");
        switch(type){
            case "create" :
                setMenu(new String[]{"next","Type","Special","next","s","t","next","Mass","Radius","next","0","0","next","Location(X)","Location(Y)","next","0","0","next","Velocity(X)","Velocity(Y)","next","0","0"});
                setButtons(new String[]{"next","CREATE","CANCEL"});
                setBodyInfo(new Planet(Planet.EARTH));
                break;
            case "settings" :
                setMenu(new String[]{"next","Gravity","Time Speed","next","0","0","next","Expand Rate","next","0"});
                setButtons(new String[]{"next","APPLY","BACK","CLEAR ALL"});
                editTexts.get(0).setText(String.valueOf(Universe.G));
                editTexts.get(1).setText(String.valueOf(Universe.TIME_SPEED));
                editTexts.get(2).setText(String.valueOf(Universe.EXPAND_RATE));
                break;
            default :
                preparedBody = getIntent().getParcelableExtra("selected");
                switch (type){
                    case "edit" :
                        if(preparedBody instanceof Star){
                            setMenu(new String[]{"next","Mass","next","0","next","Location(X)","Location(Y)","next","0","0","next","Velocity(X)","Velocity(Y)","next","0","0"});
                        }else if(preparedBody instanceof BlackHole) {
                            setMenu(new String[]{"next","Mass","next","0","next","Location(X)","Location(Y)","next","0","0","next","Velocity(X)","Velocity(Y)","next","0","0"});
                        }else if(preparedBody instanceof Burst) {
                            setMenu(new String[]{"next","Luminosity","next","0","next","Location(X)","Location(Y)","next","0","0","next","Velocity(X)","Velocity(Y)","next","0","0"});
                        }else {
                            setMenu(new String[]{"next","Mass","Radius","next","0","0","next","Location(X)","Location(Y)","next","0","0","next","Velocity(X)","Velocity(Y)","next","0","0"});
                        }
                        setButtons(new String[]{"next","EDIT","REMOVE","next","CANCEL","MANAGE"});
                        break;
                    case "edit/manage" :
                        if(preparedBody instanceof Star){
                            setMenu(new String[]{"next","Mass","Radius","next","0","0","next","Location(X)","Location(Y)","next","0","0","next","Velocity(X)","Velocity(Y)","next","0","0","next","Temperature","HEAT CAPACITY","next","0","0"});
                        }else{
                            setMenu(new String[]{"next","Mass","Radius","next","0","0","next","Location(X)","Location(Y)","next","0","0","next","Velocity(X)","Velocity(Y)","next","0","0"});
                        }
                        setButtons(new String[]{"next","EDIT","REMOVE","next","CANCEL","FREE"});
                        break;
                }
                setBodyInfo(preparedBody);
        }
        editTable = findViewById(R.id.editTable);
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams)editTable.getLayoutParams();
        Point screen = new Point();
        Display d = getWindowManager().getDefaultDisplay();
        d.getSize(screen);
        param.width = (int)(screen.x*0.9);
        param.height = (int)(screen.y*0.8);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent();
        Button b = (Button)view;
        for(EditText et : editTexts){
            if(et.getText().toString().equals("")){
                et.setText("0");
            }
        }
        switch (b.getText().toString()){
            case "CREATE" :
                if(createType.equals("struct")){
                    i.putExtra("structType", specialType);
                    i.putExtra("locationX",Double.parseDouble(editTexts.get(0).getText().toString())*Universe.AU);
                    i.putExtra("locationY",Double.parseDouble(editTexts.get(1).getText().toString())*Universe.AU);
                    i.putExtra("velocityX",Double.parseDouble(editTexts.get(2).getText().toString()));
                    i.putExtra("velocityY",Double.parseDouble(editTexts.get(3).getText().toString()));
                    setResult(Activity.RESULT_OK, i);
                }else{
                    getBodyInfo(preparedBody);
                    i.putExtra("receive",preparedBody);
                    if(preparedBody.getMass() > 0){
                        setResult(Activity.RESULT_OK, i);
                    }else{
                        setResult(Activity.RESULT_CANCELED);
                    }
                }
                i.putExtra("type","create");

                finish();
                break;
            case "EDIT" :
                getBodyInfo(preparedBody);
                i.putExtra("receive",preparedBody);
                i.putExtra("type","edit");
                if(preparedBody.getMass() > 0){
                    setResult(Activity.RESULT_OK, i);
                }else{
                    setResult(Activity.RESULT_CANCELED);
                }
                setResult(Activity.RESULT_OK, i);
                finish();
                break;
            case "CANCEL" :
                setResult(Activity.RESULT_OK, i);
                i.putExtra("type","cancel");
                finish();
                break;
            case "APPLY" :
                setResult(Activity.RESULT_OK, i);
                i.putExtra("type","apply");
                i.putExtra("gravity",editTexts.get(0).getText().toString());
                i.putExtra("time_speed",editTexts.get(1).getText().toString());
                i.putExtra("expand_rate",editTexts.get(2).getText().toString());
                i.putExtra("cleanMode",cleanMode);
                finish();
                break;
            case "BACK" :
                setResult(Activity.RESULT_OK, i);
                i.putExtra("type","cancel");
                finish();
                break;
            case "CLEAR ALL" :
                cleanMode = true;
                break;
            case "REMOVE" :
                setResult(Activity.RESULT_OK, i);
                i.putExtra("type","remove");
                finish();
                break;
            case "MANAGE" :
                setResult(Activity.RESULT_OK, i);
                i.putExtra("type","manage");
                finish();
                break;
            case "FREE" :
                setResult(Activity.RESULT_OK, i);
                i.putExtra("type","free");
                finish();
                break;
        }
    }

    public void setButtons(String[]buttonNames){
        TableLayout table = findViewById(R.id.editTable);
        TableRow row = null;
        Button button;
        TableLayout.LayoutParams param1 = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,0);
        param1.weight = 1;
        TableRow.LayoutParams param2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
        param2.weight = 1;
        param2.setMargins(5,0,10,10);
        for(String buttonName : buttonNames){
            if(buttonName.equals("next")){
                row = new TableRow(this);
                row.setLayoutParams(param1);
                table.addView(row);
            }else{
                button = new Button(this);
                button.setId(View.generateViewId());
                button.setLayoutParams(param2);
                button.setBackgroundResource(R.drawable.button_design);
                button.setGravity(Gravity.CENTER);
                button.setPadding(0,0,0,0);
                button.setText(buttonName);
                button.setOnClickListener(this);
                if(row != null){
                    row.addView(button);
                }
            }
        }
    }

    public void setMenu(String[] headerNames){
        final TableLayout table = findViewById(R.id.editTable);
        TableRow row = null;
        TextView textView;
        EditText editText;
        TableLayout.LayoutParams param1 = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,0);
        param1.weight = 1;
        TableRow.LayoutParams param2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
        param2.weight = 1;
        for(String headerName : headerNames){
            switch (headerName){
                case "next" :
                    row = new TableRow(this);
                    row.setLayoutParams(param1);
                    table.addView(row);
                    break;
                case "s" :
                    param2.setMargins(0,10,5,10);
                    Spinner spinner1 = new Spinner(this);
                    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.body_type,android.R.layout.simple_spinner_item);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(adapter1);
                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for(int i = table.getChildCount() - 1;i > 1;i--){
                                table.removeViewAt(i);
                            }
                            switch (String.valueOf(parent.getItemAtPosition(position))){
                                case "Planet" :
                                    editTexts = new ArrayList<>();
                                    setMenu(new String[]{"next","Mass","Radius","next","0","0","next","Location(X)","Location(Y)","next","0","0","next","Velocity(X)","Velocity(Y)","next","0","0"});
                                    createType = "planet";
                                    specialSpinner.setAdapter(specialPlanet);
                                    setBodyInfo(new Planet(Planet.EARTH));
                                    break;
                                case "Star" :
                                    editTexts = new ArrayList<>();
                                    setMenu(new String[]{"next","Mass","next","0","next","Location(X)","Location(Y)","next","0","0","next","Velocity(X)","Velocity(Y)","next","0","0","next"});
                                    createType = "star";
                                    specialSpinner.setAdapter(specialStar);
                                    setBodyInfo(new Star(Star.SUN));
                                    break;
                                case "BlackHole" :
                                    editTexts = new ArrayList<>();
                                    setMenu(new String[]{"next","Mass","next","0","next","Location(X)","Location(Y)","next","0","0","next","Velocity(X)","Velocity(Y)","next","0","0"});
                                    createType = "black hole";
                                    specialSpinner.setAdapter(specialBlackhole);
                                    setBodyInfo(new BlackHole(BlackHole.SAGITARRUS_A));
                                    break;
                                case "Burst" :
                                    editTexts = new ArrayList<>();
                                    setMenu(new String[]{"next","Luminosity","next","0","next","Location(X)","Location(Y)","next","0","0","next","Velocity(X)","Velocity(Y)","next","0","0"});
                                    createType = "burst";
                                    specialSpinner.setAdapter(specialBurst);
                                    setBodyInfo(new Burst(Burst.HYPER_NOVA));
                                    break;
                                case "Struct" :
                                    editTexts = new ArrayList<>();
                                    setMenu(new String[]{"next","Location(X)","Location(Y)","next","0","0","next","Velocity(X)","Velocity(Y)","next","0","0"});
                                    createType = "struct";
                                    specialSpinner.setAdapter(specialStruct);
                                    break;
                            }
                            setButtons(new String[]{"next","CREATE","CANCEL"});
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                    spinner1.setLayoutParams(param2);
                    spinner1.setGravity(Gravity.CENTER);
                    spinner1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    if(row != null){
                        row.addView(spinner1);
                    }
                    break;
                case "t" :
                    param2.setMargins(0,10,5,10);
                    specialSpinner.setAdapter(specialPlanet);
                    specialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            specialType = String.valueOf(parent.getItemAtPosition(position));
                            switch (createType){
                                case "planet" :
                                    switch (specialType){
                                        case "Mercury" :
                                            preparedBody = new Planet(Planet.MERCURY);
                                            break;
                                        case "Venus" :
                                            preparedBody = new Planet(Planet.VENUS);
                                            break;
                                        case "Earth" :
                                            preparedBody = new Planet(Planet.EARTH);
                                            break;
                                        case "Mars" :
                                            preparedBody = new Planet(Planet.MARS);
                                            break;
                                        case "Jupiter" :
                                            preparedBody = new Planet(Planet.JUPITER);
                                            break;
                                        case "Saturn" :
                                            preparedBody = new Planet(Planet.SATURN);
                                            break;
                                        case "Uranus" :
                                            preparedBody = new Planet(Planet.URANUS);
                                            break;
                                        case "Neptune" :
                                            preparedBody = new Planet(Planet.NEPTUNE);
                                            break;
                                        case "Moon" :
                                            preparedBody = new Planet(Planet.MOON);
                                            break;
                                        case "Phobos" :
                                            preparedBody = new Planet(Planet.PHOBOS);
                                            break;
                                        case "Deimos" :
                                            preparedBody = new Planet(Planet.DEIMOS);
                                            break;
                                        case "Ganymede" :
                                            preparedBody = new Planet(Planet.GANYMEDE);
                                            break;
                                        case "Callisto" :
                                            preparedBody = new Planet(Planet.CALLISTO);
                                            break;
                                        case "Europa" :
                                            preparedBody = new Planet(Planet.EUROPA);
                                            break;
                                        case "Io" :
                                            preparedBody = new Planet(Planet.IO);
                                            break;
                                        case "Titan" :
                                            preparedBody = new Planet(Planet.TITAN);
                                            break;
                                        case "Enceladus" :
                                            preparedBody = new Planet(Planet.ENCELADUS);
                                            break;
                                        case "Rhea" :
                                            preparedBody = new Planet(Planet.RHEA);
                                            break;
                                        case "Dione" :
                                            preparedBody = new Planet(Planet.DIONE);
                                            break;
                                        case "Mimas" :
                                            preparedBody = new Planet(Planet.MIMAS);
                                            break;
                                        case "Tethys" :
                                            preparedBody = new Planet(Planet.TETHYS);
                                            break;
                                        case "Iapetus" :
                                            preparedBody = new Planet(Planet.IAPETUS);
                                            break;
                                        case "Oberon" :
                                            preparedBody = new Planet(Planet.OBERON);
                                            break;
                                        case "Titania" :
                                            preparedBody = new Planet(Planet.TITANIA);
                                            break;
                                        case "Umbriel" :
                                            preparedBody = new Planet(Planet.UMBRIEL);
                                            break;
                                        case "Triton" :
                                            preparedBody = new Planet(Planet.TRITON);
                                            break;
                                        case "Pluto" :
                                            preparedBody = new Planet(Planet.PLUTO);
                                            break;
                                        case "Charon" :
                                            preparedBody = new Planet(Planet.CHARON);
                                            break;
                                        case "Ceres" :
                                            preparedBody = new Planet(Planet.CERES);
                                            break;
                                    }break;
                                case "star" :
                                    switch (specialType){
                                        case "Sun" :
                                            preparedBody = new Star(Star.SUN);
                                            break;
                                        case "Alpha Centauri A" :
                                            preparedBody = new Star(Star.ALPHA_CENTAURI_A);
                                            break;
                                        case "Alpha Centauri B" :
                                            preparedBody = new Star(Star.ALPHA_CENTAURI_B);
                                            break;
                                        case "Proxima Centauri" :
                                            preparedBody = new Star(Star.PROXIMA_CENTAURI);
                                            break;
                                        case "Wolf 359" :
                                            preparedBody = new Star(Star.WOLF_359);
                                            break;
                                        case "Sirius A" :
                                            preparedBody = new Star(Star.SIRIUS_A);
                                            break;
                                        case "Sirius B" :
                                            preparedBody = new Star(Star.SIRIUS_B);
                                            break;
                                        case "Rigel" :
                                            preparedBody = new Star(Star.RIGEL);
                                            break;
                                        case "VY Canis Majoris" :
                                            preparedBody = new Star(Star.VY_CANIS_MAJORIS);
                                            break;
                                        case "ETA Carinae A" :
                                            preparedBody = new Star(Star.ETA_CARINAE_A);
                                            break;
                                        case "Crab Pulsar" :
                                            preparedBody = new Star(Star.CRAB_PULSAR);
                                            break;
                                    }break;
                                case "black hole" :
                                    switch (specialType){
                                        case "Sagitarrus A" :
                                            preparedBody = new BlackHole(BlackHole.SAGITARRUS_A);
                                            break;
                                        case "NGC 1277" :
                                            preparedBody = new BlackHole(BlackHole.NGC_1277);
                                            break;
                                        case "S5 0014 81" :
                                            preparedBody = new BlackHole(BlackHole.S5_0014_81);
                                            break;
                                        case "GRO 1655 40" :
                                            preparedBody = new BlackHole(BlackHole.GRO_1655_40);
                                            break;
                                    }break;
                                case "burst" :
                                    switch (specialType){
                                        case "Hyper Nova" :
                                            preparedBody = new Burst(Burst.HYPER_NOVA);
                                            break;
                                        case "Super Nova" :
                                            preparedBody = new Burst(Burst.SUPER_NOVA);
                                            break;
                                        case "Gamma Ray Burst" :
                                            preparedBody = new Burst(Burst.GAMMA_RAY_BURST);
                                            break;
                                        case "Tsar Bomb" :
                                            preparedBody = new Burst(Burst.TSAR_BOMB);
                                            break;
                                    }break;
                                case "struct" :
                                    preparedBody = null;
                                    break;
                            }
                            if(preparedBody != null){
                                preparedBody.location = new Vector(getIntent().getDoubleExtra("coordX",0),getIntent().getDoubleExtra("coordY",0));
                                setBodyInfo(preparedBody);
                            }else{
                                editTexts.get(0).setText(format.format(getIntent().getDoubleExtra("coordX",0)/Universe.AU));
                                editTexts.get(1).setText(format.format(getIntent().getDoubleExtra("coordY",0)/Universe.AU));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    specialSpinner.setLayoutParams(param2);
                    specialSpinner.setGravity(Gravity.CENTER);
                    specialSpinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    if(row != null){
                        row.addView(specialSpinner);
                    }
                    break;
                case "0" :
                    param2.setMargins(0,10,5,10);
                    editText = new EditText(this);
                    editText.setId(View.generateViewId());
                    editText.setLayoutParams(param2);
                    editText.setGravity(Gravity.CENTER);
                    editText.setPadding(0,0,0,0);
                    editText.setText(headerName);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    if(row != null){
                        row.addView(editText);
                    }
                    editTexts.add(editText);
                    break;
                case "1" :
                    param2.setMargins(0,10,5,10);
                    editText = new EditText(this);
                    editText.setId(View.generateViewId());
                    editText.setLayoutParams(param2);
                    editText.setGravity(Gravity.CENTER);
                    editText.setPadding(0,0,0,0);
                    editText.setText(headerName);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    if(row != null){
                        row.addView(editText);
                    }
                    editTexts.add(editText);
                    break;
                default :
                    param2.setMargins(10,5,10,0);
                    textView = new TextView(this);
                    textView.setId(View.generateViewId());
                    textView.setLayoutParams(param2);
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(0,0,0,0);
                    textView.setText(headerName);
                    textView.setTextSize(25);
                    textView.setTextColor(getResources().getColor(R.color.colorEditMenu));
                    if(row != null){
                        row.addView(textView);
                    }
            }
        }
    }

    public void setBodyInfo(Body ob){
        if(ob != null){
            if(ob instanceof Planet){
                editTexts.get(0).setText(format.format(ob.getMass()/Universe.M_EARTH));
                editTexts.get(1).setText(format.format(ob.getRadius()/Universe.R_EARTH));
                editTexts.get(2).setText(format.format(ob.location.getX()/Universe.AU));
                editTexts.get(3).setText(format.format(ob.location.getY()/Universe.AU));
                editTexts.get(4).setText(format.format(ob.velocity.getX()));
                editTexts.get(5).setText(format.format(ob.velocity.getY()));
            }else if(ob instanceof Star){
                editTexts.get(0).setText(format.format(ob.getMass()/Universe.M));
                editTexts.get(1).setText(format.format(ob.location.getX()/Universe.AU));
                editTexts.get(2).setText(format.format(ob.location.getY()/Universe.AU));
                editTexts.get(3).setText(format.format(ob.velocity.getX()));
                editTexts.get(4).setText(format.format(ob.velocity.getY()));
            }else if(ob instanceof BlackHole){
                editTexts.get(0).setText(format.format(ob.getMass()/Universe.M));
                editTexts.get(1).setText(format.format(ob.location.getX()/Universe.AU));
                editTexts.get(2).setText(format.format(ob.location.getY()/Universe.AU));
                editTexts.get(3).setText(format.format(ob.velocity.getX()));
                editTexts.get(4).setText(format.format(ob.velocity.getY()));
            }else if(ob instanceof Burst){
                editTexts.get(0).setText(format.format(((Burst) ob).luminosity));
                editTexts.get(1).setText(format.format(ob.location.getX()/Universe.AU));
                editTexts.get(2).setText(format.format(ob.location.getY()/Universe.AU));
                editTexts.get(3).setText(format.format(ob.velocity.getX()));
                editTexts.get(4).setText(format.format(ob.velocity.getY()));
            }
        }
    }

    public void getBodyInfo(Body ob){
        if(ob != null){
            if(ob instanceof Planet){
                ob.setMass(Double.parseDouble(editTexts.get(0).getText().toString())*Universe.M_EARTH);
                ob.setRadius(Double.parseDouble(editTexts.get(1).getText().toString())*Universe.R_EARTH);
                ob.location.setX(Double.parseDouble(editTexts.get(2).getText().toString())*Universe.AU);
                ob.location.setY(Double.parseDouble(editTexts.get(3).getText().toString())*Universe.AU);
                ob.velocity.setX(Double.parseDouble(editTexts.get(4).getText().toString()));
                ob.velocity.setY(Double.parseDouble(editTexts.get(5).getText().toString()));
            }else if(ob instanceof  Star){
                ob.setMass(Double.parseDouble(editTexts.get(0).getText().toString())*Universe.M);
                ob.location.setX(Double.parseDouble(editTexts.get(1).getText().toString())*Universe.AU);
                ob.location.setY(Double.parseDouble(editTexts.get(2).getText().toString())*Universe.AU);
                ob.velocity.setX(Double.parseDouble(editTexts.get(3).getText().toString()));
                ob.velocity.setY(Double.parseDouble(editTexts.get(4).getText().toString()));
            }else if(ob instanceof  BlackHole){
                ob.setMass(Double.parseDouble(editTexts.get(0).getText().toString())*Universe.M);
                ob.location.setX(Double.parseDouble(editTexts.get(1).getText().toString())*Universe.AU);
                ob.location.setY(Double.parseDouble(editTexts.get(2).getText().toString())*Universe.AU);
                ob.velocity.setX(Double.parseDouble(editTexts.get(3).getText().toString()));
                ob.velocity.setY(Double.parseDouble(editTexts.get(4).getText().toString()));
            }else if(ob instanceof  Burst){
                ((Burst) ob).luminosity = Double.parseDouble(editTexts.get(0).getText().toString());
                ((Burst) ob).luminosityStart = Double.parseDouble(editTexts.get(0).getText().toString());
                ob.location.setX(Double.parseDouble(editTexts.get(1).getText().toString())*Universe.AU);
                ob.location.setY(Double.parseDouble(editTexts.get(2).getText().toString())*Universe.AU);
                ob.velocity.setX(Double.parseDouble(editTexts.get(3).getText().toString()));
                ob.velocity.setY(Double.parseDouble(editTexts.get(4).getText().toString()));
            }
        }
    }
}