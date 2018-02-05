package com.myapp.kirat.sqliteloginpage;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    SQLiteOpenHelper dbhelper;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //To hide AppBar for fullscreen.
        ActionBar ab = getSupportActionBar();
        ab.hide();

        //Referencing UserEmail, Password EditText and TextView for SignUp Now
        final EditText _mail = (EditText) findViewById(R.id.emailId);
        final EditText _pass = (EditText) findViewById(R.id.passId);
        Button login = (Button) findViewById(R.id.button);
        TextView _signUp = (TextView) findViewById(R.id.signUp);

        //Opening SQLite Pipeline
        dbhelper = new SqliteDBhelper(this);
        db = dbhelper.getReadableDatabase();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = _mail.getText().toString();
                String pass = _pass.getText().toString();

                cursor = db.rawQuery("SELECT *FROM " + SqliteDBhelper.TABLE_NAME + " WHERE " + SqliteDBhelper.COLUMN_EMAIL + "=? AND " + SqliteDBhelper.COLUMN_PASSWORD + "=?", new String[]{mail, pass});
                if (cursor != null) {
                    if (cursor.getCount() > 0) {

                        cursor.moveToFirst();
                        //Retrieving User FullName and Email after successfull login and passing to LoginSucessActivity
                        String _name = cursor.getString(cursor.getColumnIndex(SqliteDBhelper.COLUMN_FULLNAME));
                        String _email = cursor.getString(cursor.getColumnIndex(SqliteDBhelper.COLUMN_EMAIL));
                        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, LoginSuccessActivity.class);
                        intent.putExtra("fullname", _name);
                        intent.putExtra("email", _email);
                        startActivity(intent);

                        //Removing MainActivity[Login Screen] from the stack for preventing back button press.
                        finish();
                    } else {

                        //I am showing Alert Dialog Box here for alerting user about wrong credentials
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Alert");
                        builder.setMessage("Username or Password is wrong.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();

                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        //-------Alert Dialog Code Snippet End Here
                    }
                }

            }
        });
        // Intent For Opening RegisterAccountActivity
        _signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, RegisterNewActivity.class);
                startActivity(intent);
            }
        });
    }

}