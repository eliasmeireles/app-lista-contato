package systemplus.com.br.listadecontatos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.Collections;
import java.util.List;

import systemplus.com.br.listadecontatos.adapter.ContacAdapter;
import systemplus.com.br.listadecontatos.core.ContactQuery;
import systemplus.com.br.listadecontatos.model.Contact;

import static systemplus.com.br.listadecontatos.extra.AppExtraKey.CONTACT_EXTRA_KEY;

public class ListContactAcitivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private boolean backHome = false;
    private List<Contact> contactList;
    private RecyclerView contactListRecyclerView;
    private FloatingActionButton actionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(null);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        actionButton = findViewById(R.id.fab_add_contact);
        contactListRecyclerView = findViewById(R.id.recycler_view_contact_list);


        ContactQuery contactQuery = new ContactQuery(this, null);
        contactList = contactQuery.find();


        findViewById(R.id.added_contact).setVisibility(View.GONE);

        actionButton.setOnClickListener(view -> {
            startActivityForResult(new Intent(ListContactAcitivity.this, ContactCadastroActivity.class), 1);
        });

        setContactOnView();

    }

    private void setContactOnView() {
        if (contactList != null && contactList.size() > 0) {

            Collections.sort(contactList, (firstContact, secondContact) -> firstContact.getNome().compareTo(secondContact.getNome()));

            RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(ListContactAcitivity.this,
                    1, GridLayoutManager.VERTICAL, false);
            ContacAdapter contacAdapter = new ContacAdapter(ListContactAcitivity.this, contactList);

            contactListRecyclerView.setLayoutManager(gridLayoutManager);
            contactListRecyclerView.setAdapter(contacAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().findItem(R.id.contact_list).setVisible(false);
        navigationView.getMenu().findItem(R.id.got_to_home).setVisible(true);
        setContactOnView();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer(drawer);
        } else {
            super.onBackPressed();
        }

        if (backHome) {
            super.onBackPressed();
        }

        backHome = false;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_contact:
                startActivityForResult(new Intent(ListContactAcitivity.this, ContactCadastroActivity.class), 1);
                break;

            case R.id.got_to_home:
                backHome = true;
                this.onBackPressed();
                break;
            default:
                break;
        }

        closeDrawer(drawer);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Contact contact = (Contact) data.getSerializableExtra(CONTACT_EXTRA_KEY);
                contactList.add(contact);

                actionButton.setVisibility(View.GONE);
                findViewById(R.id.added_contact).setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> {
                    actionButton.setVisibility(View.VISIBLE);
                    findViewById(R.id.added_contact).setVisibility(View.GONE);
                }, 3000);

                setContactOnView();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void closeDrawer(DrawerLayout drawer) {
        drawer.closeDrawer(GravityCompat.START);
    }
}
