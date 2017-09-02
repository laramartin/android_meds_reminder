package eu.laramartin.medsreminder.meds;

import eu.laramartin.medsreminder.model.Med;

public class MedsAdapterItem {

    private boolean isExpanded = false;
    private Med med;

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public Med getMed() {
        return med;
    }

    public void setMed(Med med) {
        this.med = med;
    }

    @Override
    public String toString() {
        return "MedsAdapterItem{" +
                "isExpanded=" + isExpanded +
                ", med=" + med +
                '}';
    }
}
