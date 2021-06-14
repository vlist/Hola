package com.vlist.holaenhanced;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private final String TAG = "LoginFragment";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    private String userEmail;
    private String userPassword;

    TextInputEditText mPassword;
    TextInputEditText mEmail;
    MaterialButton mBack;
    MaterialButton mNext;
    TextInputLayout mPasswordLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);



        mNext = (MaterialButton) view.findViewById(R.id.next_button);
        mBack = (MaterialButton) view.findViewById(R.id.cancel_button);
        mEmail = (TextInputEditText) view.findViewById(R.id.email_edit_text);
        mPassword = (TextInputEditText) view.findViewById(R.id.password_edit_text);
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
                    //Toast.makeText(getActivity(),(mEmail.getText().toString() + mPassword.getText()),Toast.LENGTH_SHORT).show();

                    userEmail = mEmail.getText().toString();
                    userPassword = mPassword.getText().toString();

                    mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                try {
                                    Toast.makeText(getActivity(), "sign in error", Toast.LENGTH_SHORT).show();
                                    Log.i(TAG, "Login Failed ");
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            }else{
                                Intent intent = new Intent(getContext().getApplicationContext(),MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
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