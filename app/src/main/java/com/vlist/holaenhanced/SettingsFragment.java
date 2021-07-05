package com.vlist.holaenhanced;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private final String TAG = "SettingFragment";

    private MaterialButton mBack, mFinish, mLogout;
    private TextView mUsername, mGender, mEmail, mDescription, mPhoneNumber;
    private ImageView mProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private String profileImageUrl;
    private String userId;
    private String phoneNumber;

    private Uri resultUri;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mFinish = (MaterialButton) view.findViewById(R.id.setting_finish);
        mBack = (MaterialButton) view.findViewById(R.id.setting_cancel);
        mLogout = (MaterialButton) view.findViewById(R.id.setting_logout);
        mUsername = (TextView) view.findViewById(R.id.username_placeholder);
        mEmail = (TextView) view.findViewById(R.id.email_placeholder);
        mGender = (TextView) view.findViewById(R.id.gender_placeholder);
        mDescription = (TextView) view.findViewById(R.id.description_placeholder);
        mPhoneNumber = (TextView) view.findViewById(R.id.phone_placeholder);
        mProfileImage = (ImageView) view.findViewById(R.id.user_profile_image);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new PlaceholderFragment())
                        .commit();
            }
        });

        getUserInfo();

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
                getUserInfo();
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        return view;
    }

    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") != null) {
                        mUsername.setText(map.get("name").toString());
                    }
                    if (map.get("sex") != null) {
                        mGender.setText(map.get("sex").toString());
                    }
                    Glide.with(mProfileImage.getContext()).clear(mProfileImage);
                    if (map.get("profileImageUrl") != null) {
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch (profileImageUrl) {
                            case "default":
                                Glide.with(mProfileImage.getContext()).load(R.mipmap.ic_launcher).into(mProfileImage);
                                break;
                            default:
                                Glide.with(mProfileImage.getContext()).load(profileImageUrl).into(mProfileImage);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void uploadProfileImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (resultUri != null) {
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;
            try {
                Bitmap oriBitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplication().getContentResolver(), resultUri);
                if (oriBitmap.getWidth() >= oriBitmap.getHeight()) {
                    bitmap = Bitmap.createBitmap(oriBitmap, oriBitmap.getWidth() / 2 - oriBitmap.getHeight() / 2, 0,
                            oriBitmap.getHeight(),
                            oriBitmap.getHeight()
                    );
                } else {
                    bitmap = Bitmap.createBitmap(oriBitmap, 0, oriBitmap.getHeight() / 2 - oriBitmap.getWidth() / 2,
                            oriBitmap.getWidth(),
                            oriBitmap.getWidth()
                    );
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "uploadTask failure");
                    Toast.makeText(getActivity(), "uploadTask failure", Toast.LENGTH_SHORT).show();
                }
            });

            Bitmap finalBitmap = bitmap;
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.w(TAG, "uploadTask success");
                    mProfileImage.setImageBitmap(finalBitmap);
                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri imageUri) {
                            Map userInfo = new HashMap();
                            userInfo.put("profileImageUrl", imageUri.toString());
                            mUserDatabase.updateChildren(userInfo);

                            Toast.makeText(getActivity(), "uploadTask success", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private void saveUserInformation() {
        String name = mUsername.getText().toString();
        String phone = mPhoneNumber.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("name", name);
        userInfo.put("phone", phone);
        mUserDatabase.updateChildren(userInfo);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            uploadProfileImage();

        }
    }

    public void logoutUser() {
        mAuth.signOut();
        Intent intent = new Intent(getActivity().getApplicationContext(), StarterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}