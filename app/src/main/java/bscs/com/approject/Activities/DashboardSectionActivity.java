package bscs.com.approject.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import bscs.com.approject.Classes.Teacher;
import bscs.com.approject.R;

public class DashboardSectionActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView specializationTextView;
    private ListView sectionListview;
    private View view;

    static Uri imgUri;
    static String sectionName;

    ArrayList<String> sections;
    private String[] content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_section);
        initialize();
        settingValues();
        sections = TeacherDashboardActivity.section;
        content = sections.toArray(new String[0]);
        ResponseCustomListviewAdapter adapter = new ResponseCustomListviewAdapter();
        sectionListview.setAdapter(adapter);
    }

    private void callingNextActivity() {


    }

    private void settingValues() {
        nameTextView.setText(TeacherDashboardActivity.name);
        emailTextView.setText(TeacherDashboardActivity.email);
        specializationTextView.setText(TeacherDashboardActivity.specialization);
    }

    private void initialize() {
        nameTextView = findViewById(R.id.profileName1Textview);
        emailTextView = findViewById(R.id.profileEmail1Textview);
        specializationTextView = findViewById(R.id.profileSpecialization1Textview);
        sectionListview = findViewById(R.id.sectionListview);
        sections = new ArrayList<>();
    }

    class ResponseCustomListviewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return content.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.dashboard_section_display, null);
            final TextView sectionNameTextView = view.findViewById(R.id.sectionName);
            sectionNameTextView.setText(content[position].toUpperCase());
            sectionName = sectionNameTextView.getText().toString().toLowerCase();
            // Toast.makeText(DashboardSectionActivity.this, "Clicked: "+sectionName, Toast.LENGTH_LONG).show();

            sectionNameTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(DashboardSectionActivity.this);
                }
            });
            return view;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imgUri = result.getUri();
                Toast.makeText(DashboardSectionActivity.this, "" + imgUri, Toast.LENGTH_LONG).show();
               // callingNextActivity();
                Intent intent = new Intent(DashboardSectionActivity.this, TeacherDashboardActivity.class);
                startActivity(intent);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
