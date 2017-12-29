package systemplus.com.br.listadecontatos;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import systemplus.com.br.listadecontatos.adapter.ContacAdapter;
import systemplus.com.br.listadecontatos.core.ContactQuery;
import systemplus.com.br.listadecontatos.dialog.ContactCustomDialog;
import systemplus.com.br.listadecontatos.model.Contact;

import static systemplus.com.br.listadecontatos.extra.AppCodeKey.CREATE_CONTACT_CODE;
import static systemplus.com.br.listadecontatos.extra.AppCodeKey.UPDATE_CONTACT_CODE;
import static systemplus.com.br.listadecontatos.extra.AppExtraKey.CONTACT_EXTRA_KEY;

public class ListContactAcitivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private boolean backHome = false;
    private List<Contact> contactList;
    private RecyclerView contactListRecyclerView;
    private FloatingActionButton actionButton;
    private Snackbar snackbar;
    private ContacAdapter contacAdapter;


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

        viewsActions();

        setContactOnView();

    }

    private void viewsActions() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        actionButton = findViewById(R.id.fab_add_contact);
        contactListRecyclerView = findViewById(R.id.recycler_view_contact_list);
        actionButton.setOnClickListener(view -> startCadastro());
    }

    private void startCadastro() {
        chechSnack();
        startActivityForResult(new Intent(ListContactAcitivity.this, ContactCadastroActivity.class), CREATE_CONTACT_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().findItem(R.id.contact_list).setVisible(false);
        navigationView.getMenu().findItem(R.id.got_to_home).setVisible(true);


        if (contactList == null || contactList.size() == 0) {
            snackbar = Snackbar.make(actionButton, getResources().getString(R.string.contact_list_empty), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.snackbar_leave), view1 -> {
                    });

            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_action);
            textView.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
            snackbar.show();
        }
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

        switch (requestCode) {
            case CREATE_CONTACT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    getDataResult(data, getResources().getString(R.string.contact_added));
                }
                break;

            case UPDATE_CONTACT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    getDataResult(data, getResources().getString(R.string.contact_updated));
                }
                break;
        }

        setContactOnView();
    }


    private void setContactOnView() {
        ContactQuery contactQuery = new ContactQuery(this, null);
        contactList = contactQuery.findAll();


        if (contactList != null && contactList.size() > 0) {

            Collections.sort(contactList, (firstContact, secondContact) -> firstContact.getNome().compareTo(secondContact.getNome()));

            RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(ListContactAcitivity.this,
                    1, GridLayoutManager.VERTICAL, false);
            contacAdapter = new ContacAdapter(ListContactAcitivity.this, contactList);

            contactListRecyclerView.setLayoutManager(gridLayoutManager);
            contactListRecyclerView.setAdapter(contacAdapter);
            contactListRecyclerView.invalidate();
        }
    }
    private void getDataResult(Intent data, String snackbarMessage) {
        Contact contact = (Contact) data.getSerializableExtra(CONTACT_EXTRA_KEY);
        contactList.add(contact);

        DialogFragment newFragment = ContactCustomDialog.newInstance(contact);

        newFragment.show(getFragmentManager(), "dialog");
        chechSnack();

        snackbar = Snackbar.make(actionButton, snackbarMessage, Snackbar.LENGTH_LONG);

        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }

    private void chechSnack() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    private void closeDrawer(DrawerLayout drawer) {
        drawer.closeDrawer(GravityCompat.START);
    }
}
