package bottomnavigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.quanlychitieu.AboutActivity;
import com.example.quanlychitieu.ChangePassword;
import com.example.quanlychitieu.LoginActivity;
import com.example.quanlychitieu.MyWallet;
import com.example.quanlychitieu.PrivacyActivity;
import com.example.quanlychitieu.R;
import com.example.quanlychitieu.SendMailActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class ProfileFrag extends Fragment {

    TextView logOut, myWalletLink, aboutLink, contactfb, privacyLink, changePass,SendMail;
    FirebaseAuth mAuth;

    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_profile, container, false);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myWalletLink = view.findViewById(R.id.manageWallet);
        contactfb = view.findViewById(R.id.contact_fb);
        logOut = view.findViewById(R.id.logoutBtn);
        mAuth = FirebaseAuth.getInstance();
        aboutLink = view.findViewById(R.id.about);
        privacyLink = view.findViewById(R.id.privacy);
        changePass = view.findViewById(R.id.changePassword);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

                getActivity().finish();

                Toast.makeText(getActivity(), "You have been signed out.", Toast.LENGTH_SHORT).show();
            }
        });

        myWalletLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyWallet.class);
                startActivity(intent);

//                getActivity().finish();
            }
        });

        aboutLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);

//                getActivity().finish();
            }
        });

        privacyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrivacyActivity.class);
                startActivity(intent);

//                getActivity().finish();
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePassword.class);
                startActivity(intent);

//                getActivity().finish();
            }
        });

        SendMail = view.findViewById(R.id.SendMail);

        SendMail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SendMailActivity.class);
                startActivity(intent);
            }
        });

    }

}