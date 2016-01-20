package me.yogeshwardan.gvcsmartdoor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ui.ParseLoginDispatchActivity;

public class Dispatch extends ParseLoginDispatchActivity{

    @Override
    protected Class<?> getTargetClass() {
        return Doors.class;
    }

}
