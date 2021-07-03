package com.vlist.holaenhanced.LoginAndRegister;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vlist.holaenhanced.MainActivity;
import com.vlist.holaenhanced.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private final String TAG = "RegisterFragment";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    MaterialButton mNext;
    MaterialButton mBack;

    RadioGroup mGenderGroup;

    EditText mEmail;
    EditText mUsername;
    EditText mPassword;

    TextInputLayout mPasswordLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mAuth = FirebaseAuth.getInstance();
        mBack = (MaterialButton) view.findViewById(R.id.cancel_button);
        mNext = (MaterialButton) view.findViewById(R.id.next_button);

        mEmail = (EditText) view.findViewById(R.id.email_edit_text);
        mPassword = (EditText) view.findViewById(R.id.password_edit_text);
        mUsername = (EditText) view.findViewById(R.id.username_edit_text);
        mGenderGroup = (RadioGroup) view.findViewById(R.id.gender_group);
        mPasswordLayout = (TextInputLayout) view.findViewById(R.id.password_text_input);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Back button onClick");
                Toast.makeText(getActivity(),"Back to choose fragment",Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.starter_content,new ChooseFragment(),null)
                        .commit();
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPasswordValid(mPassword.getText()) || mEmail.getText() == null) {
                    mPasswordLayout.setError(getString(R.string.error_password));
                } else {
                    mPasswordLayout.setError(null);

                    String password = mPassword.getText().toString();
                    String username = mUsername.getText().toString();
                    String email = mEmail.getText().toString();

                    String gender;

                    int selectedGender = mGenderGroup.getCheckedRadioButtonId();

                    if(selectedGender == R.id.gender_male){
                        gender = "Male";
                    }else{
                        gender = "Female";
                    }

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getActivity(), "sign up error", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Failed " + task.toString());
                            } else {
                                try {
                                    String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                    Log.i(TAG, "userId = " + userId);
                                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                    Map userInfo = new HashMap<>();
                                    userInfo.put("name", username);
                                    userInfo.put("sex", gender);
                                    userInfo.put("profileImageUrl", "default");
                                    currentUserDb.updateChildren(userInfo);

                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG,e.getMessage());
                                }
                            }
                        }
                    });

                }
            }
        });

        return view;
    }

    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }
}