package com.example.shahboz.homerental.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shahboz.homerental.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddApartment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int PLACE_PICKER_REQUEST = 1500;
    private static final String TAG = "PlaceAutocomplete";
    private ExpandableGridView photos;
    private List<String> myList;
    private String mCurrentPhotoPath;
    private GoogleApiClient mGoogleApiClient;
    private EditText edit;
    private LatLng query_location;

    private PlaceAutocompleteAdapter mAdapter;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    public AddApartment() {
        // Required empty public constructor
    }
    public static AddApartment newInstance() {
        AddApartment fragment = new AddApartment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myList = new ArrayList<String>();
        myList.clear();
        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();

        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_apartment, container, false);
        Spinner rooms = (Spinner)rootView.findViewById(R.id.room_spinner);
        Integer[] array = {1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(),android.R.layout.simple_spinner_dropdown_item,array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rooms.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, getActivity()));
        rooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button addphoto = (Button)rootView.findViewById(R.id.add_photo);
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] items = {"Take Photo", "Pick from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add Photo");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //camera
                            takePicture();
                        } else if (which == 1) {
                            //gallery
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2000);
                        }
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //hashtag edittext
        final Linkify.TransformFilter filter = new Linkify.TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return match.group();
            }
        };

        final Pattern hashtagPattern = Pattern.compile("#+[A-Za-z0-9_-]+");
        final String hashtagScheme = "content://com.example.shahboz.homerental/";


        edit = (EditText)rootView.findViewById(R.id.edit_tags);

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Linkify.addLinks(s, hashtagPattern, hashtagScheme, null, filter);

            }
        });
        //list of images

        photos = (ExpandableGridView)rootView.findViewById(R.id.gridView);
        photos.setAdapter(new ImageAdapter(getActivity()));
        photos.setExpanded(true);

        //address autocompletion
        AutoCompleteTextView address = (AutoCompleteTextView)rootView.findViewById(R.id.address);
        address.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(getActivity(), android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
        address.setAdapter(mAdapter);

        Button showMap = (Button)rootView.findViewById(R.id.show_map);
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                builder.setLatLngBounds(new LatLngBounds(query_location,query_location));
                Context context = getActivity().getApplicationContext();
                try {
                    startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                    .setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getStatus().isSuccess()) {
                                final Place myPlace = places.get(0);
                                query_location = myPlace.getLatLng();
                            }
                            places.release();
                        }
                    });


        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 1000:
                if(resultCode == Activity.RESULT_OK){
                    // Save Image To Gallery
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
                    File f = new File(mCurrentPhotoPath );
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    getActivity().sendBroadcast(mediaScanIntent);

                    // Add Image Path To List
                    myList.add( mCurrentPhotoPath);

                    // Refresh Gridview Image Thumbnails
                    photos.invalidateViews();
                }

                break;
            case 2000:
                if(resultCode == Activity.RESULT_OK){
                    Uri selectedImage = data.getData();
                    myList.add(getRealPathFromURI(selectedImage));
                    photos.invalidateViews();
                }
                break;
            case PLACE_PICKER_REQUEST:
                if(resultCode == Activity.RESULT_OK){
                    Place place = PlacePicker.getPlace(data, getActivity());
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    private void takePicture()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
        {
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }
            catch (IOException ex)
            {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
                takePictureIntent.putExtra(MediaStore. EXTRA_OUTPUT,
                        Uri. fromFile(photoFile));
                startActivityForResult(takePictureIntent, 1000);
            }
        }
    }
    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format( new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" ;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File. createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(getActivity(),
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    public class ImageAdapter extends BaseAdapter
    {
        private Context mContext;
        private final LayoutInflater mInflater;
        public ImageAdapter(Context c)
        {
            mContext = c;
            mInflater = LayoutInflater.from(mContext);
        }
        public int getCount()
        {
            return myList.size();
        }
        public Object getItem(int position)
        {
            return null;
        }
        public long getItemId(int position)
        {
            return 0;
        }
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View v = convertView;
            ImageView picture;


            if (v == null) {
                v = mInflater.inflate(R.layout.image, parent, false);
                v.setTag(R.id.picture, v.findViewById(R.id.picture));

            }

            picture = (ImageView) v.getTag(R.id.picture);

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions. inJustDecodeBounds = false ;
            bmOptions. inSampleSize = 4;
            bmOptions. inPurgeable = true ;
            Bitmap bitmap = BitmapFactory.decodeFile(myList.get(position), bmOptions);

            picture.setImageBitmap(bitmap);

            return v;
        }
    }

}
