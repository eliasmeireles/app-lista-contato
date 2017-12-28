package systemplus.com.br.listadecontatos.model;

/**
 * Created by elias on 27/12/17.
 */

public class ContextMenuItem {

    private int icon;
    private String label;


    public ContextMenuItem(int icon, String label) {
        this.icon = icon;
        this.label = label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
