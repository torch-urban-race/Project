package com.example.torchapp.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.torchapp.R;
import com.example.torchapp.SaveSharedPreference;
import com.example.torchapp.database.DatabaseFacade;
import com.example.torchapp.map.DrawerMapActivity;
import com.example.torchapp.model.CurrentUser;
import com.google.android.gms.maps.model.LatLng;

public class TorchCreatorFragment extends Fragment {


   private TextInputLayout torchCreatorTorchNameInputLayout;
   private TextInputEditText torchCreatorTorchNameInputText;
   private Button createTorchButton;
   private Button cancelButton;
   private RadioButton publicRadioButton;
   private RadioButton privateRadioButton;
   private DrawerMapActivity drawerMapActivity;

   public TorchCreatorFragment(){
       drawerMapActivity = UIUtils.getInstanceIfExists().drawerMapActivity;

   }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_torch_creator, container, false);

        //Layout
        torchCreatorTorchNameInputLayout = (TextInputLayout) v.findViewById(R.id.torch_creator_textInputLayout);
        //Actual text the user fills in
        torchCreatorTorchNameInputText = (TextInputEditText) v.findViewById(R.id.torch_creator_textInputEditText);

        createTorchButton = (Button) v.findViewById(R.id.torch_creator_create_button);
        cancelButton = (Button) v.findViewById(R.id.torch_creator_cancel_button);

        privateRadioButton = (RadioButton) v.findViewById(R.id.torch_set_private);
        publicRadioButton = (RadioButton) v.findViewById(R.id.torch_set_private);


        createTorchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(torchCreatorTorchNameInputText.getText().length() < 3){
                    torchCreatorTorchNameInputLayout.setError("Torch name needs to be at least 3 characters.");
                } else if(privateRadioButton.isChecked()) {
                    LatLng pos = new LatLng(drawerMapActivity.mLastKnownLocation.getLatitude(), drawerMapActivity.mLastKnownLocation.getLongitude());

                    DatabaseFacade.createTorch(drawerMapActivity, torchCreatorTorchNameInputText.getText().toString(), pos.latitude, pos.longitude, SaveSharedPreference.getUserName(drawerMapActivity.getApplicationContext()), false);
                    torchCreatorTorchNameInputText.setText("");
                    fragmentRemoveSelf();

                } else {
                    LatLng pos = new LatLng(drawerMapActivity.mLastKnownLocation.getLatitude(), drawerMapActivity.mLastKnownLocation.getLongitude());

                    DatabaseFacade.createTorch(drawerMapActivity, torchCreatorTorchNameInputText.getText().toString(), pos.latitude, pos.longitude, SaveSharedPreference.getUserName(drawerMapActivity.getApplicationContext()), true);
                    torchCreatorTorchNameInputText.setText("");
                    fragmentRemoveSelf();

                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    fragmentRemoveSelf();

            }

        });



        return v;
    }

    public void fragmentRemoveSelf(){
        drawerMapActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
    }



}