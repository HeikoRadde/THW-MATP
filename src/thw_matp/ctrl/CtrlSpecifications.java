/*
    Copyright (c) 2021 Heiko Radde
    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
    documentation files (the "Software"), to deal in the Software without restriction, including without limitation
    the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
    to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of
    the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
    THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
    TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package thw_matp.ctrl;

import thw_matp.datatypes.Specification;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for actions related to specifications
 */
public class CtrlSpecifications {

    /**
     * @param db Database to be used by this controller
     */
    public CtrlSpecifications(Database db) {
        this.db = db;
    }

    /**
     * @return Get data related to all specifications in a ready-to-use format for tables
     */
    public DefaultTableModel get_data() {
        DefaultTableModel mdl = new DefaultTableModel();
        mdl.setColumnIdentifiers(new String[]{"Sachnr", "Vorschrift", "Abschnitt", "Link"});
        List<Specification> vorschriften = null;
        try {
            vorschriften = this.db.specifications_get_all();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(vorschriften != null) {
            for (Specification specification : vorschriften) {
                mdl.addRow(new Object[]{specification.sachnr, specification.vorschrift, specification.abschnitt, specification.link});
            }
        }
        else {
            System.err.println("No entry in table vorschriften!");
        }
        return mdl;
    }

    /**
     *                  Get Data of a certain specification
     * @param sachnr    Unique ID of the specification to retrieve
     * @return          Ready-to-use format for tables with the specification, null of specification wasn't found
     */
    public Specification get_specification(String sachnr) {
        try {
            return this.db.specification_get(sachnr);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.err.println("No entry in table vorschriften!");
            return null;
        }
    }

    /**
     * @return Get list with all available Sachnummern
     */
    public List<String> get_sachnummern() {
        List<String> ret = new ArrayList<>();
        try {
            List<Specification> vorschriften = this.db.specifications_get_all();
            for (Specification specification : vorschriften) {
                ret.add(specification.sachnr);
            }
            ret = ret.stream().sorted().collect(Collectors.toList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ret;
    }

    /**
     *                          Remove a certain specification and all Items and inspections related to it
     * @param sachnummer        Unique Sachnummer of the specification
     * @param ctrl_inventar     Controller for the inventory
     * @param ctrl_pruefungen   Controller for the inspections
     * @return                  True if removal was successfull, false if it failed
     */
    public boolean remove_specification(String sachnummer, CtrlInventory ctrl_inventar, CtrlInspections ctrl_pruefungen) {
        try {
            if (!ctrl_inventar.remove_items(sachnummer, ctrl_pruefungen)) return false;
            this.db.specification_remove(sachnummer);
        } catch (SQLException throwables) {
            System.err.println("Failed to remove entry " + sachnummer + " in table vorschriften!");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *                      Add a specification to the database
     * @param sachnummer    Unique Sachnummer of the specification
     * @param vorschrift    Name of the specification
     * @param abschnitt     [Optional] Section of the specification
     * @param link          [Optional] Link to the specification
     * @return
     */
    public int add_specification(String sachnummer, String vorschrift, String abschnitt, String link) {
        try {
            this.db.specification_add(sachnummer, link, vorschrift, abschnitt);
        } catch (SQLException e) {
            if (e.getErrorCode() == Database.DUPLICATE_KEY_1) {
                System.err.println("Sachnummer " + sachnummer +  " already existing!");
                return 1;
            }
            else {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     *                      Edit an existing specification
     * @param sachnummer    Unique Sachnummer of the specification
     * @param vorschrift    Potentially modified name of the specification
     * @param abschnitt     Potentially modified section of the specification
     * @param link          Potentially modified link to the specification
     * @return
     */
    public boolean edit_specification(String sachnummer, String vorschrift, String abschnitt, String link) {
        try {
            this.db.specification_update(sachnummer, link, vorschrift, abschnitt);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Initialise the database with known specifications
     */
    public void init_db() {
        try {
            this.db.specification_add("2510T91100", "E", "", "");
            this.db.specification_add("3411T91203", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("3415T91204", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("3431T00207", "Schweißgerät.htm", "GUV-V A3", "EN 60974-4");
            this.db.specification_add("3431T91007", "E", "", "");
            this.db.specification_add("3439T91238", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("3439T91239", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("3695T22269", "Krankentransport.htm", "GUV-G 9102", "GUV-G 9102 Kap. 20");
            this.db.specification_add("3695T22270", "Krankentransport.htm", "GUV-G 9102", "GUV-G 9102 Kap. 20");
            this.db.specification_add("3695T23268", "Krankentransport.htm", "GUV-G 9102", "GUV-G 9102 Kap. 20");
            this.db.specification_add("3815T00002", "Kloben_Haken.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3820T23293", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("3940T00001", "Seilzug.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T00002", "Zurrgurte.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T00004", "LOG", "", "");
            this.db.specification_add("3940T00018", "Zurrgurte.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T00019", "Kloben_Haken.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T00037", "LOG", "", "");
            this.db.specification_add("3940T00039", "LOG", "", "");
            this.db.specification_add("3940T00044", "LOG", "", "");
            this.db.specification_add("3940T00046", "LOG", "", "");
            this.db.specification_add("3940T11387", "Zurrgurte.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T21349", "Zurrgurte.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("3940T21351", "Zurrgurte.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T22187", "Zurrgurte.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T22189", "Zurrgurte.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T22316", "Kloben_Haken.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T22317", "Kloben_Haken.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T22318", "Kloben_Haken.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T22340", "Zurrgurte.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T22350", "Zurrgurte.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T23318", "Kloben_Haken.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T31250", "Zurrgurte.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T33187", "Zurrgurte.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3940T33188", "Zurrgurte.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("3950T332323", "Kloben_Haken.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("4010T00001", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T00002", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4010T00011", "LOG", "", "");
            this.db.specification_add("4010T21333", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4010T22200", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T22326", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T22327", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T22328", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T22333", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4010T22334", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4010T22337", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4010T22338", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T23335", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4010T23336", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4010T23341", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4010T23342", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4010T23343", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4010T23344", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4010T23345", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4010T23346", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4010T23999", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4010T24325", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T24327", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T24330", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T24332", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T26329", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T26331", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T26332", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T26333", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T26334", "Kette.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4010T26335", "Zurrkette.htm", "GUV-R 500", "GUV-R500 Kap. 2.8");
            this.db.specification_add("4020T22140", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22240", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22260", "LOG", "", "");
            this.db.specification_add("4020T22345", "Hebeband.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4020T22354", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22355", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22356", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22357", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22358", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22359", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22360", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22361", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22362", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22363", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22364", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22365", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22366", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22367", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22368", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22369", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22370", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22371", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22372", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22373", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22374", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22375", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22376", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22377", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22378", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22379", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22380", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T22381", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T23351", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T23352", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T23353", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T23357", "Rundschlingen.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T23373", "Kloben_Haken.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("4020T24352", "Rundschlingen.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4020T24999", "Drahtseil.htm", "GUV-R 151", "GUV-R 151");
            this.db.specification_add("4020T26346", "Hebeband.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T26347", "Hebeband.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T26348", "Hebeband.htm", "GUV-R 500", "GUV-R500-2.8");
            this.db.specification_add("4020T26357", "LOG", "", "");
            this.db.specification_add("4020T32344", "Kloben_Haken.htm", "GUV-R 500", "GUV-R 500, Kap. 2.8");
            this.db.specification_add("4020T34348", "Hebeband.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4030T00001", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4030T00002", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4030T22360", "Anschlagstueck.htm", "GUV-R 500", "GUV-R 500-2.8");
            this.db.specification_add("4030T22364", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4030T22365", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4030T22366", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4030T22367", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4030T22368", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4030T22370", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4030T22371", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4030T23364", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4030T23365", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4030T23368", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4030T26367", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4030T70910", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("4310T23800", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("4310T91483", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("4320T22492", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("4320T22493", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("4320T23489", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("4320T34490", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("4320T34491", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("4320T34494", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("4320T34495", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("4320T34497", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("4320T34498", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("4520T11800", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("4940T33537", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5110T91647", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5120T22913", "Seilzug.htm", "GUV-V D8", "Herstellervorgabe");
            this.db.specification_add("5120T23009", "Kettenzug.htm", "GUV-V D8", "Herstellervorgabe");
            this.db.specification_add("5120T23675", "Seilzug.htm", "GUV-V D8", "Herstellervorgabe");
            this.db.specification_add("5120T24009", "Kettenzug.htm", "GUV-V D8", "Herstellervorgabe");
            this.db.specification_add("5130T00103", "T", "", "");
            this.db.specification_add("5130T22919", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T22920", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T22921", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T22928", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T22930", "trennschleifer.htm", "GUV-G 9102", "GUV-G 9102 Kap. 20");
            this.db.specification_add("5130T23801", "E", "", "");
            this.db.specification_add("5130T23852", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T23927", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T23929", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T31922", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T91560", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T91660", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T91918", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T91920", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T91924", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T91927", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T91938", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5130T91940", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5411T12520", "Zurrkette.htm", "GUV-R 500", "GUV-R500 Kap. 2.8");
            this.db.specification_add("5440T00011", "T", "", "");
            this.db.specification_add("5440T22261", "kombileiter.htm", "GUV-V D36", "GUV-G 9102 Kap.8");
            this.db.specification_add("5440T22262", "T", "", "");
            this.db.specification_add("5440T22263", "T", "", "");
            this.db.specification_add("5440T22264", "T", "", "");
            this.db.specification_add("5440T22267", "windstuetzen.htm", "GUV-V D 8", "Herstellerangaben");
            this.db.specification_add("5440T22268", "windstuetzen.htm", "GUV-V D 8", "Herstellerangaben");
            this.db.specification_add("5440T22269", "windstuetzen.htm", "GUV-V D 8", "Herstellerangaben");
            this.db.specification_add("5440T22620", "Schaekel.htm", "GUV-R 500", "GUV-R500 - 2.8");
            this.db.specification_add("5440T23265", "T", "", "");
            this.db.specification_add("5440T31258", "leitern.htm", "GUV-V D36", "GUV-G 9102 K 7-9");
            this.db.specification_add("5805T00100", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5805T11006", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5805T11017", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5920T11702", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("5925T23933", "E", "", "");
            this.db.specification_add("5935T21328", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6115T10005", "Stromerzeuger.htm", "GUV-V A3", "DIN VDE 0701-0702 u.");
            this.db.specification_add("6115T22051", "Stromerzeuger.htm", "GUV-V A3", "DIN VDE 0701-0702 u.");
            this.db.specification_add("6115T22360", "Stromerzeuger.htm", "GUV-V A3", "DIN VDE 0701-0702 u.");
            this.db.specification_add("6115T22361", "Stromerzeuger.htm", "GUV-V A3", "DIN VDE 0701-0702 u.");
            this.db.specification_add("6115T32123", "E", "", "");
            this.db.specification_add("6115T32125", "E", "", "");
            this.db.specification_add("6130T00004", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6130T00007", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6130T00017", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6130T00027", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6130T00029", "E", "", "");
            this.db.specification_add("6130T00324", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6130T21363", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6130T21365", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6130T21609", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6130T60002", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6130T91363", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6140T11033", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T11704", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T11705", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T11710", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T21328", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T21329", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T22137", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T22337", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T22437", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T22438", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T22439", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T22537", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T22637", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T22999", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T23133", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T23233", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T23235", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T23387", "Baustromverteiler.htm", "GUV-V A3", "VDE 0701-0702/0100-6");
            this.db.specification_add("6150T23433", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T25219", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T31127", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T31328", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6150T31827", "Baustromverteiler.htm", "GUV-V A3", "VDE 0701-0702/0100-6");
            this.db.specification_add("6150T37377", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6230T00012", "E", "", "");
            this.db.specification_add("6230T00161", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6230T00401", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6230T11402", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6230T11403", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6230T11706", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6230T22399", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6230T22400", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6230T22401", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6230T22409", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6230T22419", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6530T00481", "T", "", "");
            this.db.specification_add("6530T22477", "T", "", "");
            this.db.specification_add("6530T22479", "T", "", "");
            this.db.specification_add("6545T00001", "sanitätskasten.htm", "GUV-G 9102", "Inhaltsverzeicnis");
            this.db.specification_add("6545T21484", "sanitätskasten.htm", "GUV-G 9102", "Inhaltsverzeicnis");
            this.db.specification_add("6545T22000", "sanitätskasten.htm", "GUV-G 9102", "Inhaltsverzeicnis");
            this.db.specification_add("6545T22010", "sanitätskasten.htm", "GUV-G 9102", "Inhaltsverzeicnis");
            this.db.specification_add("6625T31529", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("6650T32506", "E", "", "");
            this.db.specification_add("6695T02000", "E", "", "");
            this.db.specification_add("6730T11101", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7021T00002", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7021T00004", "E", "", "");
            this.db.specification_add("7021T00025", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7025T00010", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7025T00016", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7025T00020", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7025T00035", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7025T00040", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7025T00041", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7025T00042", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7025T00044", "E", "", "");
            this.db.specification_add("7025T00045", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7025T00047", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7025T11011", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7025T11101", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7195T00500", "E", "", "");
            this.db.specification_add("7290T01000", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7310T00010", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7310T91410", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7520T00010", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7520T00100", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7910T31656", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("7910T60300", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
            this.db.specification_add("8110T91300", "ElektrogeräteAlggemein.htm", "GUV-V A3", "VDE 0701-0702");
        } catch (SQLException throwables) {
//            throwables.printStackTrace();
        }

    }



    private final Database db;
}
