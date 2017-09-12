package eu.laramartin.medsreminder.common;

import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.model.Med;
import eu.laramartin.medsreminder.model.Report;

public abstract class MedsUtility {

    public static int getMedIcon(String selectedDosage) {
        int iconId;
        switch (selectedDosage.toLowerCase()) {
            case "liquid":
                iconId = R.drawable.ic_liquid_128;
                break;
            case "syrup":
                iconId = R.drawable.ic_syrup_128;
                break;
            case "injection":
                iconId = R.drawable.ic_injection_128;
                break;
            case "suppository":
                iconId = R.drawable.ic_suppository_128;
                break;
            case "drop":
                iconId = R.drawable.ic_drops_128;
                break;
            case "inhaler":
                iconId = R.drawable.ic_inhaler_128;
                break;
            case "topic":
                iconId = R.drawable.ic_cream_128;
                break;
            case "capsule":
            default:
                iconId = R.drawable.ic_capsule_128;
                break;
        }
        return iconId;
    }

    public static Report getReportFromMed(Med med) {
        // TODO: 12.09.17 Lara: format date for report
        // TODO: 12.09.17 Lara: replace hardcoded date with formatted date
        Report report = new Report(
                med.getName(),
                "hardcoded date"
        );
        return report;
    }
}
