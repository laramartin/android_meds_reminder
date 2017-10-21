/*
 * PROJECT LICENSE
 *
 * This project was submitted by Lara Martín as part of the Nanodegree At Udacity.
 *
 * As part of Udacity Honor code, your submissions must be your own work, hence
 * submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account.
 *
 * Me, the author of the project, allow you to check the code as a reference, but if
 * you submit it, it's your own responsibility if you get expelled.
 *
 * Copyright (c) 2017 Lara Martín
 *
 * Besides the above notice, the following license applies and this license notice
 * must be included in all works derived from this project.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package eu.laramartin.medsreminder.common;

import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.model.Med;
import eu.laramartin.medsreminder.model.Report;

import static eu.laramartin.medsreminder.common.CalendarUtility.getFormattedCurrentDateWithHour;

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
        String formattedDate = getFormattedCurrentDateWithHour();
        Report report = new Report();
        report.setMedName(med.getName());
        report.setTimeTaken(formattedDate);
        return report;
    }
}
