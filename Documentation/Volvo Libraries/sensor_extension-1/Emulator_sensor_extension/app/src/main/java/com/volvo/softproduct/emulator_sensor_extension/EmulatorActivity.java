package com.volvo.softproduct.emulator_sensor_extension;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.volvo.softproduct.emulatorextensionlibrary.db_enum.art_data;
import com.volvo.softproduct.emulatorextensionlibrary.db_enum.exc_data;
import com.volvo.softproduct.emulatorextensionlibrary.db_enum.wlo_data;
import com.volvo.softproduct.emulatorextensionlibrary.db_enum.gnss_data;
import com.volvo.softproduct.emulatorextensionlibrary.managers.*;
import com.volvo.softproduct.emulatorextensionlibrary.db_value.*;

public class EmulatorActivity extends Activity {

    private machine_manager _mdm;
    private gnss_manager _gdm;

    public String[] machineData;
    public ArrayAdapter<String> machineData_Adapter;
    private Handler handlerMachineData;
    private Handler handlerGNSSData;

    public ImageView machineType;
    public ImageView machineGlow;

    private RadioButton _artRb;
    private RadioButton _wloRb;
    private RadioButton _excRb;

    private TextView _gps_status;
    private TextView _gpsageofdiff;
    private TextView _gpsheading;
    private TextView _gpsheading_status;
    private TextView _gpsposition;
    private TextView _gpssop_cog;
    private TextView _gpssop_cog_status;
    private TextView _gpstime;
    private TextView  _timeSlot;

    public int actionSelect = -1;

    private Runnable gnssRunnable = new Runnable() {

        @Override
        public void run()
        {
            handlerGNSSData.postDelayed(gnssRunnable, 100);

            long_value lvalue;
            flag_value value;
            float_value dvalue;

            if((_gdm.isTimeScopeGNSSPassed())&&(_mdm.isTimeScopeCAN4Passed())) {
                _mdm.restartReplay();
                _gdm.restartReplay();
            }

            lvalue = _gdm.getLongSignal(gnss_data.gnssTime.getCode());
            _gpstime.setText(String.format("Time %d\n", lvalue.getValue()));

            value = _gdm.getFlagSignal(gnss_data.gnssQuality.getCode());
            _gps_status.setText(String.format("GnssQuality %d\n",value.getValue()));

            dvalue = _gdm.getFloatSignal(gnss_data.ageOfDiff.getCode());
            _gpsageofdiff.setText(String.format("AgeOfDiff %.5f\n",dvalue.getValue()));

            float_value dvaluelat = _gdm.getFloatSignal(gnss_data.latitude.getCode());
            float_value dvaluelong = _gdm.getFloatSignal(gnss_data.longitude.getCode());
            float_value dvaluealt = _gdm.getFloatSignal(gnss_data.altitude.getCode());
            _gpsposition.setText(String.format("Lat %.5f Long %.5f Alt %.5f\n",dvaluelat.getValue(),dvaluelong.getValue(),dvaluealt.getValue()));

            value = _gdm.getFlagSignal(gnss_data.headingSqi.getCode());
            dvalue = _gdm.getFloatSignal(gnss_data.heading.getCode());
            _gpsheading.setText(String.format("Heading %.5f\n", dvalue.getValue()));
            _gpsheading_status.setText(String.format("HeadingQuality %d\n", value.getValue()));

            value = _gdm.getFlagSignal(gnss_data.cogSogSqi.getCode());
            float_value dvaluecourse = _gdm.getFloatSignal(gnss_data.courseOverGround.getCode());
            float_value dvaluespeed = _gdm.getFloatSignal(gnss_data.speedOverGround.getCode());
            _gpssop_cog.setText(String.format("COG %.5f SOG %.5f", dvaluecourse.getValue(),dvaluespeed.getValue()));
            _gpssop_cog_status.setText(String.format("COGSOGQuality %d\n", value.getValue()));
        }
    };

    private Runnable runnableMachineData = new Runnable() {

        @Override
        public void run()
        {
            handlerMachineData.postDelayed(runnableMachineData, 100);
            for(int i=0;i<20;i++) {
                machineData[i] = "";
            }
            if((_gdm.isTimeScopeGNSSPassed())&&(_mdm.isTimeScopeCAN4Passed())) {
                _mdm.restartReplay();
                _gdm.restartReplay();
            }


            _timeSlot.setText("Current time: " + _mdm.getCurrentTime());

            if(actionSelect == 0) { //ART
                long_value lvalue;
                flag_value value;
                float_value dvalue;
                value = _mdm.getFlagSignal(art_data.Dump_body_up.getCode());
                machineData[0] = String.format("%s %d [%s]", art_data.Dump_body_up.name().toString(), value.getValue(), value.getStatus().name());
                value = _mdm.getFlagSignal(art_data.Unloaded.getCode());
                machineData[1] = String.format("%s %d [%s]", art_data.Unloaded.name().toString(), value.getValue(), value.getStatus().name());
                lvalue = _mdm.getLongSignal(art_data.Active_gear.getCode());
                long gearValue = lvalue.getValue();
                if(gearValue < 0)
                    machineData[2] = String.format("%s R-%d [%s]", art_data.Active_gear.name().toString(), gearValue * -1 , lvalue.getStatus().name());
                else if(gearValue == 0)
                    machineData[2] = String.format("%s N [%s]", art_data.Active_gear.name().toString(), lvalue.getStatus().name());
                else if(gearValue > 0)
                    machineData[2] = String.format("%s F-%d [%s]", art_data.Active_gear.name().toString(), gearValue , lvalue.getStatus().name());

                dvalue = _mdm.getFloatSignal(art_data.Current_load.getCode());
                machineData[3] = String.format("%s %.2f [%s]", art_data.Current_load.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(art_data.Engine_speed.getCode());
                machineData[4] = String.format("%s %.2f [%s]", art_data.Engine_speed.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(art_data.Fuel_level.getCode());
                machineData[5] = String.format("%s %.2f [%s]", art_data.Fuel_level.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(art_data.Instant_fuel_consumption.getCode());
                machineData[6] = String.format("%s %.2f [%s]", art_data.Instant_fuel_consumption.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                lvalue = _mdm.getLongSignal(art_data.Machine_hours.getCode());
                machineData[7] = String.format("%s %d [%s]", art_data.Machine_hours.name().toString(), lvalue.getValue(), lvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(art_data.Outdoor_temp.getCode());
                machineData[8] = String.format("%s %.2f [%s]", art_data.Outdoor_temp.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                value = _mdm.getFlagSignal(art_data.Parking_break_applied.getCode());
                machineData[9] = String.format("%s %d [%s]", art_data.Parking_break_applied.name().toString(), value.getValue(), value.getStatus().name());
                value = _mdm.getFlagSignal(art_data.Red_central_warning.getCode());
                machineData[10] = String.format("%s %d [%s]", art_data.Red_central_warning.name().toString(), value.getValue(), value.getStatus().name());
                dvalue = _mdm.getFloatSignal(art_data.Steering_angle.getCode());
                machineData[11] = String.format("%s %.2f [%s]", art_data.Steering_angle.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(art_data.Vehicle_speed.getCode());
                machineData[12] = String.format("%s %.2f [%s]", art_data.Vehicle_speed.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(art_data.Weight_in_bucket.getCode());
                machineData[13] = String.format("%s %.2f [%s]", art_data.Weight_in_bucket.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                value = _mdm.getFlagSignal(art_data.Yellow_central_warning.getCode());
                machineData[14] = String.format("%s %d [%s]", art_data.Yellow_central_warning.name().toString(), value.getValue(), value.getStatus().name());

                machineData_Adapter.notifyDataSetChanged();
            }
            else if(actionSelect == 1) { //WLO
                long_value lvalue;
                flag_value value;
                float_value dvalue;
                dvalue = _mdm.getFloatSignal(wlo_data.Boom_angle.getCode());
                machineData[0] = String.format("%s %.2f [%s]", wlo_data.Boom_angle.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(wlo_data.Tilt_angle.getCode());
                machineData[1] = String.format("%s %.2f [%s]", wlo_data.Tilt_angle.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                lvalue = _mdm.getLongSignal(wlo_data.Active_gear.getCode());
                long gearValue = lvalue.getValue();
                if(gearValue < 0)
                    machineData[2] = String.format("%s R-%d [%s]", wlo_data.Active_gear.name().toString(), gearValue * -1 , lvalue.getStatus().name());
                else if(gearValue == 0)
                    machineData[2] = String.format("%s N [%s]", wlo_data.Active_gear.name().toString(), lvalue.getStatus().name());
                else if(gearValue > 0)
                    machineData[2] = String.format("%s F-%d [%s]", wlo_data.Active_gear.name().toString(), gearValue , lvalue.getStatus().name());

                dvalue = _mdm.getFloatSignal(wlo_data.Current_load.getCode());
                machineData[3] = String.format("%s %.2f [%s]", wlo_data.Current_load.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(wlo_data.Engine_speed.getCode());
                machineData[4] = String.format("%s %.2f [%s]", wlo_data.Engine_speed.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(wlo_data.Fuel_level.getCode());
                machineData[5] = String.format("%s %.2f [%s]", wlo_data.Fuel_level.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(wlo_data.Instant_fuel_consumption.getCode());
                machineData[6] = String.format("%s %.2f [%s]", wlo_data.Instant_fuel_consumption.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                lvalue = _mdm.getLongSignal(wlo_data.Machine_hours.getCode());
                machineData[7] = String.format("%s %d [%s]", wlo_data.Machine_hours.name().toString(), lvalue.getValue(), lvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(wlo_data.Outdoor_temp.getCode());
                machineData[8] = String.format("%s %.2f [%s]", wlo_data.Outdoor_temp.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                value = _mdm.getFlagSignal(wlo_data.Parking_break_applied.getCode());
                machineData[9] = String.format("%s %d [%s]", wlo_data.Parking_break_applied.name().toString(), value.getValue(), value.getStatus().name());
                value = _mdm.getFlagSignal(wlo_data.Red_central_warning.getCode());
                machineData[10] = String.format("%s %d [%s]", wlo_data.Red_central_warning.name().toString(), value.getValue(), value.getStatus().name());
                dvalue = _mdm.getFloatSignal(wlo_data.Steering_angle.getCode());
                machineData[11] = String.format("%s %.2f [%s]", wlo_data.Steering_angle.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(wlo_data.Vehicle_speed.getCode());
                machineData[12] = String.format("%s %.2f [%s]", wlo_data.Vehicle_speed.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(wlo_data.Weight_in_bucket.getCode());
                machineData[13] = String.format("%s %.2f [%s]", wlo_data.Weight_in_bucket.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                value = _mdm.getFlagSignal(wlo_data.Yellow_central_warning.getCode());
                machineData[14] = String.format("%s %d [%s]", wlo_data.Yellow_central_warning.name().toString(), value.getValue(), value.getStatus().name());
                value = _mdm.getFlagSignal(wlo_data.BSS_active.getCode());
                machineData[15] = String.format("%s %d [%s]", wlo_data.BSS_active.name().toString(), value.getValue(), value.getStatus().name());
                value = _mdm.getFlagSignal(wlo_data.CDC_active.getCode());
                machineData[16] = String.format("%s %d [%s]", wlo_data.CDC_active.name().toString(), value.getValue(), value.getStatus().name());
                value = _mdm.getFlagSignal(wlo_data.Tool_lock_active.getCode());
                machineData[17] = String.format("%s %d [%s]", wlo_data.Tool_lock_active.name().toString(), value.getValue(), value.getStatus().name());

                machineData_Adapter.notifyDataSetChanged();
            }
            else if(actionSelect == 2) { //EXC
                long_value lvalue;
                flag_value value;
                float_value dvalue;
                dvalue = _mdm.getFloatSignal(exc_data.Current_load.getCode());
                machineData[0] = String.format("%s %.2f [%s]", exc_data.Current_load.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(exc_data.Engine_speed.getCode());
                machineData[1] = String.format("%s %.2f [%s]", exc_data.Engine_speed.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(exc_data.Fuel_level.getCode());
                machineData[2] = String.format("%s %.2f [%s]", exc_data.Fuel_level.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(exc_data.Instant_fuel_consumption.getCode());
                machineData[3] = String.format("%s %.2f [%s]", exc_data.Instant_fuel_consumption.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                lvalue = _mdm.getLongSignal(exc_data.Machine_hours.getCode());
                machineData[4] = String.format("%s %d [%s]", exc_data.Machine_hours.name().toString(), lvalue.getValue(), lvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(exc_data.Outdoor_temp.getCode());
                machineData[5] = String.format("%s %.2f [%s]", exc_data.Outdoor_temp.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                value = _mdm.getFlagSignal(exc_data.Parking_break_applied.getCode());
                machineData[6] = String.format("%s %d [%s]", exc_data.Parking_break_applied.name().toString(), value.getValue(), value.getStatus().name());
                value = _mdm.getFlagSignal(exc_data.Red_central_warning.getCode());
                machineData[7] = String.format("%s %d [%s]", exc_data.Red_central_warning.name().toString(), value.getValue(), value.getStatus().name());
                dvalue = _mdm.getFloatSignal(exc_data.Vehicle_speed.getCode());
                machineData[8] = String.format("%s %.2f [%s]", exc_data.Vehicle_speed.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                dvalue = _mdm.getFloatSignal(exc_data.Weight_in_bucket.getCode());
                machineData[9] = String.format("%s %.2f [%s]", exc_data.Weight_in_bucket.name().toString(), dvalue.getValue(), dvalue.getStatus().name());
                value = _mdm.getFlagSignal(exc_data.Yellow_central_warning.getCode());
                machineData[10] = String.format("%s %d [%s]", exc_data.Yellow_central_warning.name().toString(), value.getValue(), value.getStatus().name());
                value = _mdm.getFlagSignal(exc_data.Tool_lock_active.getCode());
                machineData[11] = String.format("%s %d [%s]", exc_data.Tool_lock_active.name().toString(), value.getValue(), value.getStatus().name());

                machineData_Adapter.notifyDataSetChanged();
            }
            else {
                machineData_Adapter.notifyDataSetChanged();
            }

        }
    };


    OnClickListener radio_listener = new OnClickListener()
    {
        public void onClick(View v)
        {
            RadioButton rb = (RadioButton) v;
            ListView dataView = (ListView) findViewById(R.id.machineDataList);
            switch(rb.getId())
            {
                case R.id.radioArt:
                    actionSelect = 0;
                    _artRb.setChecked(true);
                    _wloRb.setChecked(false);
                    _excRb.setChecked(false);
                    dataView.setBackgroundResource(R.drawable.artbg);
                    machineType.setBackgroundResource(R.drawable.art);
                    machineGlow.setBackgroundResource(R.drawable.glowart);
                    break;
                case R.id.radioWlo:
                    actionSelect = 1;
                    _artRb.setChecked(false);
                    _wloRb.setChecked(true);
                    _excRb.setChecked(false);
                    dataView.setBackgroundResource(R.drawable.wlobg);
                    machineType.setBackgroundResource(R.drawable.wlo);
                    machineGlow.setBackgroundResource(R.drawable.glowwlo);
                    break;
                case R.id.radioExc:
                    actionSelect = 2;
                    _artRb.setChecked(false);
                    _wloRb.setChecked(false);
                    _excRb.setChecked(true);
                    dataView.setBackgroundResource(R.drawable.excbg);
                    machineType.setBackgroundResource(R.drawable.exc);
                    machineGlow.setBackgroundResource(R.drawable.glowexc);
                    break;
            }
            Log.d("Emulator test App", "actionSelect = " + String.valueOf(actionSelect));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emulator_activity_main);

        machineType = (ImageView) findViewById(R.id.machineType);
        machineGlow = (ImageView) findViewById(R.id.machineGlow);

        _timeSlot = (TextView) findViewById(R.id.timeSlot);
        _gps_status = (TextView) findViewById(R.id.gps_status);
        _gpsageofdiff = (TextView) findViewById(R.id.gpsageofdiff);
        _gpsheading = (TextView) findViewById(R.id.gpsheading);
        _gpsheading_status = (TextView) findViewById(R.id.gpsheading_status);
        _gpsposition = (TextView) findViewById(R.id.gpsposition);
        _gpssop_cog = (TextView) findViewById(R.id.gpssop_cog);
        _gpssop_cog_status = (TextView) findViewById(R.id.gpssop_cog_status);
        _gpstime = (TextView) findViewById(R.id.gpstime);

        _artRb = (RadioButton) findViewById(R.id.radioArt);
        _artRb.setOnClickListener(radio_listener);

        _wloRb = (RadioButton) findViewById(R.id.radioWlo);
        _wloRb.setOnClickListener(radio_listener);

        _excRb = (RadioButton) findViewById(R.id.radioExc);
        _excRb.setOnClickListener(radio_listener);

        _artRb.setChecked(true);
        _wloRb.setChecked(false);
        _excRb.setChecked(false);

        actionSelect = 0;

        machineData = new String[20];
        for(int i=0;i<20;i++) {
            machineData[i] = "";
        }
        machineData_Adapter = new ArrayAdapter<String>(this, R.layout.list_row, machineData);
        ListView dataView = (ListView) findViewById(R.id.machineDataList);
        dataView.setAdapter(machineData_Adapter);

        handlerMachineData = new Handler();
        handlerGNSSData = new Handler();

        _mdm = new machine_manager(this);
        _gdm = new gnss_manager(this);

        if(_mdm.Connect() == true) {
            handlerMachineData.post(runnableMachineData);
        }
        if(_gdm.Connect() == true) {
            handlerGNSSData.post(gnssRunnable);
        }

    }

    public void onClickQuitBtn(View view) {
        _mdm.Disconnect();
        finish();
    }
}
