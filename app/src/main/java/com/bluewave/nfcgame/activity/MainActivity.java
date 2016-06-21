package com.bluewave.nfcgame.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.bluewave.nfcgame.R;
import com.bluewave.nfcgame.base.BaseActivity;
import com.bluewave.nfcgame.common.ActivityStarter;
import com.bluewave.nfcgame.common.Dialoger;
import com.bluewave.nfcgame.common.Global;
import com.bluewave.nfcgame.model.User;
import com.bluewave.nfcgame.net.Client;
import com.bluewave.nfcgame.net.UserClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_join)
    void onClickJoin()
    {
        String nickname = etName.getText().toString();

        if(TextUtils.isEmpty(nickname))
        {
            showToast(R.string.input_nickname);
            return;
        }
        else
        {
            UserClient.login(nickname, new Client.Handler() {
                @Override
                public void onSuccess(Object object) {
                    showToast(R.string.success_join);
                    Global.setLoginUser((User)object);
                    finish();
                    startHomeActivity();
                }

                @Override
                public void onFail() {
                    showToast(R.string.fail_join);
                }
            }, getProgressDialog());
        }
    }

    @OnClick(R.id.btn_rule)
    void onClickRule()
    {

    }


}
