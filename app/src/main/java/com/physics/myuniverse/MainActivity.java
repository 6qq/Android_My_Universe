package com.physics.myuniverse;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener{
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    Universe universe;
    SeekBar zoomBar;
    Intent editIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        universe = findViewById(R.id.MyUniverse);
        editIntent = new Intent(MainActivity.this,EditActivity.class);
        universe.create = findViewById(R.id.create);
        universe.create.setOnClickListener(this);
        universe.pause = findViewById(R.id.pause);
        universe.pause.setOnClickListener(this);
        universe.settings = findViewById(R.id.settings);
        universe.settings.setOnClickListener(this);

        Planet.init(getResources());
        Star.init(getResources());
        BlackHole.init(getResources());
        Burst.init(getResources());

        zoomBar = findViewById(R.id.zoomBar);
        zoomBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                universe.sight = Math.pow(10,2 + progress/10.0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
            }
        });

        if(universe.selectedBody != null){
            Intent i = getIntent();
            universe.selectedBody = i.getParcelableExtra("receive");
        }
        if(savedInstanceState != null){
            ArrayList<Body>bodies = savedInstanceState.getParcelableArrayList("bodies");
            if(bodies != null){
                for(int i = 0;i < bodies.size();i++){
                    universe.addBody(bodies.get(i));
                }
                universe.selectedBody = savedInstanceState.getParcelable("selectedBody");
                universe.sight = savedInstanceState.getDouble("sight");
                universe.god.setX(savedInstanceState.getDouble("locationX"));
                universe.god.setY(savedInstanceState.getDouble("locationY"));
                universe.isPaused = savedInstanceState.getBoolean("isPaused");
                universe.manageMod = savedInstanceState.getBoolean("manageMod");
                universe.pause.setText(savedInstanceState.getString("pauseButtonStatus"));
                universe.create.setText(savedInstanceState.getString("createButtonStatus"));
                zoomBar.setProgress((int)(Math.log(universe.sight) / Math.log(10) - 2)*10);
            }
        }else{
            zoomBar.setProgress(60);
            universe.addStruct(Universe.generateSolarSystem());
        }
        Planet easterEgg = new Planet(Planet.THE_DEATH_STAR);
        easterEgg.setLocation(new Vector(10e12,8e12));
        universe.addBody(easterEgg);
        universe.run();
        universe.setOnTouchListener(this);
        universe.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout userPanel = findViewById(R.id.userPanel);
                RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams)userPanel.getLayoutParams();
                param.height = (int)(universe.getHeight()*0.15);
            }
        });

    }

    @Override
    public boolean onTouch(View view, MotionEvent e) {
        double x,y;
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN :
                x = (e.getX() - universe.getWidth()/2)*universe.sight;
                y = (e.getY() - universe.getHeight()/2)*universe.sight;
                Vector touchPoint = new Vector(universe.god.getX() + x,universe.god.getY() + y);
                if(!universe.manageMod){
                    universe.selectedBody = universe.isContainBody(touchPoint);
                    if(universe.selectedBody != null){
                        universe.god = new Vector(touchPoint.getX(),touchPoint.getY());
                        universe.create.setText(R.string.edit);
                    }else{
                        universe.god = new Vector(touchPoint.getX(),touchPoint.getY());
                        universe.create.setText(R.string.create);
                    }
                }else{
                    if(universe.isPaused){
                        universe.selectedBody.setLocation(touchPoint);
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        Button source = (Button)view;
        switch (view.getId()){
            case R.id.create :
                if(universe.create.getText().toString().equals("CREATE")){
                    editIntent.putExtra("type","create");
                    editIntent.putExtra("coordX",universe.god.getX());
                    editIntent.putExtra("coordY",universe.god.getY());
                    startActivityForResult(editIntent, SECOND_ACTIVITY_REQUEST_CODE);
                }else if(universe.create.getText().toString().equals("EDIT")){
                    if(!universe.manageMod){
                        editIntent.putExtra("type","edit");
                        editIntent.putExtra("selected",universe.selectedBody);
                        startActivityForResult(editIntent, SECOND_ACTIVITY_REQUEST_CODE);
                    }else{
                        editIntent.putExtra("type","edit/manage");
                        editIntent.putExtra("selected",universe.selectedBody);
                        startActivityForResult(editIntent, SECOND_ACTIVITY_REQUEST_CODE);
                    }
                }
                break;
            case R.id.pause :
                if(source.getText().toString().equals("PAUSE")){
                    source.setText(R.string.start);
                    universe.isPaused = true;
                }else{
                    source.setText(R.string.pause);
                    universe.isPaused = false;
                }
                break;
            case R.id.settings :
                editIntent.putExtra("type","settings");
                startActivityForResult(editIntent, SECOND_ACTIVITY_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    String type = data.getStringExtra("type");
                    Body ob;
                    switch (type){
                        case "create" :
                            ob = data.getParcelableExtra("receive");
                            if(ob != null){
                                universe.addBody(ob);
                            }else{
                                AstronomicalStruct struct = null;

                                double locX = data.getDoubleExtra("locationX",0);
                                double locY = data.getDoubleExtra("locationY",0);
                                double velX = data.getDoubleExtra("velocityX",0);
                                double velY = data.getDoubleExtra("velocityY",0);

                                switch (data.getStringExtra("structType")){
                                    case "Solar System and Moons" :
                                        struct = Universe.generateSolarSystemAndMoons();
                                        break;
                                    case "Solar System" :
                                        struct = Universe.generateSolarSystem();
                                        break;
                                    case "Earth and Moon" :
                                        struct = Universe.generateEarthSystem();
                                        break;
                                    case "Mars and Moons" :
                                        struct = Universe.generateMarsSystem();
                                        break;
                                    case "Jupiter and Moons" :
                                        struct = Universe.generateJupiterSystem();
                                        break;
                                    case "Saturn and Moons" :
                                        struct = Universe.generateSaturnSystem();
                                        break;
                                    case "Uranus and Moons" :
                                        struct = Universe.generateUranusSystem();
                                        break;
                                    case "Neptune and Moons" :
                                        struct = Universe.generateNeptuneSystem();
                                        break;
                                }
                                if(struct != null){
                                    struct.setLocation(new Vector(locX,locY));
                                    struct.setVelocity(new Vector(velX,velY));
                                    universe.addStruct(struct);
                                }

                            }
                            break;
                        case "edit" :
                            ob = data.getParcelableExtra("receive");
                            universe.selectedBody.setMass(ob.getMass());
                            universe.selectedBody.setRadius(ob.getRadius());
                            universe.selectedBody.setLocation(ob.getLocation());
                            universe.selectedBody.setVelocity(ob.getVelocity());
                            if(ob instanceof Star){
                                ((Star) universe.selectedBody).setTemperature(((Star) ob).getTemperature());
                            }
                            break;
                        case "remove" :
                            universe.removeBody(universe.selectedBody);
                            break;
                        case "cancel" :

                            break;
                        case "apply" :
                            Universe.G = Double.parseDouble(data.getStringExtra("gravity"));
                            Universe.TIME_SPEED = (!data.getStringExtra("time_speed").equals("") && Double.parseDouble(data.getStringExtra("time_speed")) > 0)? Double.parseDouble(data.getStringExtra("time_speed")) : 1;
                            Universe.EXPAND_RATE = Double.parseDouble(data.getStringExtra("expand_rate"));
                            if(data.getBooleanExtra("cleanMode",false)){
                                for(int i = universe.bodies.size() - 1;i >= 0;i--){
                                    universe.removeBody(universe.bodies.get(i));
                                }
                                universe.sight = 1;
                                universe.god = new Vector(0,0);
                                Universe.TIME_SPEED = 1;
                            }
                            break;
                        case "manage" :
                            universe.manageMod = true;
                            break;
                        case "free" :
                            universe.manageMod = false;
                            break;
                    }
                }
            }
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("bodies",universe.bodies);
        outState.putParcelable("selectedBody",universe.selectedBody);
        outState.putDouble("sight",universe.sight);
        outState.putDouble("locationX",universe.god.getX());
        outState.putDouble("locationY",universe.god.getY());
        outState.putBoolean("isPaused",universe.isPaused);
        outState.putBoolean("manageMod",universe.manageMod);
        outState.putString("pauseButtonStatus",universe.pause.getText().toString());
        outState.putString("createButtonStatus",universe.create.getText().toString());
    }
}
