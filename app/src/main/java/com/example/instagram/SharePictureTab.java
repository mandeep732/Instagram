package com.example.instagram;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class  SharePictureTab extends Fragment implements View.OnClickListener {

    ImageView imageView;
    EditText editText;
    Button button;

    public SharePictureTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        imageView = v.findViewById(R.id.imageViewShare);
        editText = v.findViewById(R.id.editTextShare);
        button = v.findViewById(R.id.buttonShare);

        imageView.setOnClickListener(this);
        button.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageViewShare:
                if(Build.VERSION.SDK_INT>=23 &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1000);
                }else
                {
                    loadImageFromExternalStorage();
                }

                break;
            case R.id.buttonShare:

        }
    }

    private void loadImageFromExternalStorage() {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 2000);
    }


    //This method will run when user response to dialog box of grand permission.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1000)
        {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                loadImageFromExternalStorage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2000)
        {
            Log.i("app",resultCode+" ");
            if(resultCode == Activity.RESULT_OK){
                //Accessing the image code is different for Fragment and Activity
                try{
                    Uri selectedImage = data.getData();
                    String filePathColumn[]={MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn,null,null,null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap receivedImageBitmap = BitmapFactory.decodeFile(picturePath);
                    imageView.setImageBitmap(receivedImageBitmap) ;
                    imageView.setMaxHeight(260);

                } catch (Exception e)
                {
                   Toast.makeText(getContext(),"Error while loading! "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}
