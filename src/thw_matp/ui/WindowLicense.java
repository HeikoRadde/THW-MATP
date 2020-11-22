/*
    Copyright (c) 2020 Heiko Radde
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
package thw_matp.ui;

import javax.swing.*;

public class WindowLicense extends JFrame {
    private JPanel root_panel;
    private JTextPane txt_licence_program;
    private JTextPane txt_license_pdfbox;
    private JTextPane txt_license_common_csv;
    private JTextPane txt_license_h2database;
    private JLabel lbl_license_program;
    private JLabel lbl_license_pdfbox;
    private JLabel lbl_license_h2database;
    private JLabel lbl_license_common_csv;
    private JTextPane txt_disclaimer_h2database;

    public WindowLicense() {
        super("Lizenzen");
        this.setContentPane(this.get_root_panel());

        write_license_program();
        write_license_common_csv();
        write_license_h2database();
        write_license_pdfbox();
    }

    public JPanel get_root_panel() {
        return root_panel;
    }


    private void write_license_program() {
        lbl_license_program.setText("Lizenz zum Programm THW-MATP");
        write_license_mit(this.txt_licence_program);
        this.txt_licence_program.setCaretPosition(0);
    }

    private void write_license_pdfbox() {
        lbl_license_pdfbox.setText("Apache PDFBox 2.0.21 Lizenz");
        write_license_apace(this.txt_license_pdfbox);
        this.txt_license_pdfbox.setCaretPosition(0);
    }

    private void write_license_common_csv() {
        lbl_license_common_csv.setText("Apache Commons CSV 1.8");
        write_license_apace(this.txt_license_common_csv);
        this.txt_license_common_csv.setCaretPosition(0);
    }

    private void write_license_h2database() {
        lbl_license_h2database.setText("H2 Database Engine Version 1.4.200");
        txt_disclaimer_h2database.setText(
            "This software contains unmodified binary redistributions for\n" +
            "H2 database engine (https://h2database.com/),\n" +
            "which is dual licensed and available under the MPL 2.0\n" +
            "(Mozilla Public License) or under the EPL 1.0 (Eclipse Public License).\n" +
            "An original copy of the license agreement can be found at:\n" +
            "https://h2database.com/html/license.html"
        );
        write_license_mozilla(this.txt_license_h2database);
        this.txt_license_h2database.setCaretPosition(0);
        this.txt_disclaimer_h2database.setCaretPosition(0);
    }

    private void write_license_apace(JTextPane txt) {
        txt.setText(
            "Apache License\n" +
            "\n" +
            "Version 2.0, January 2004\n" +
            "\n" +
            "http://www.apache.org/licenses/\n" +
            "\n" +
            "TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION\n" +
            "\n" +
            "1. Definitions.\n" +
            "\n" +
            "\"License\" shall mean the terms and conditions for use, reproduction, and distribution as defined by Sections 1 through 9 of this document.\n" +
            "\n" +
            "\"Licensor\" shall mean the copyright owner or entity authorized by the copyright owner that is granting the License.\n" +
            "\n" +
            "\"Legal Entity\" shall mean the union of the acting entity and all other entities that control, are controlled by, or are under common control with that entity. For the purposes of this definition, \"control\" means (i) the power, direct or indirect, to cause the direction or management of such entity, whether by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the outstanding shares, or (iii) beneficial ownership of such entity.\n" +
            "\n" +
            "\"You\" (or \"Your\") shall mean an individual or Legal Entity exercising permissions granted by this License.\n" +
            "\n" +
            "\"Source\" form shall mean the preferred form for making modifications, including but not limited to software source code, documentation source, and configuration files.\n" +
            "\n" +
            "\"Object\" form shall mean any form resulting from mechanical transformation or translation of a Source form, including but not limited to compiled object code, generated documentation, and conversions to other media types.\n" +
            "\n" +
            "\"Work\" shall mean the work of authorship, whether in Source or Object form, made available under the License, as indicated by a copyright notice that is included in or attached to the work (an example is provided in the Appendix below).\n" +
            "\n" +
            "\"Derivative Works\" shall mean any work, whether in Source or Object form, that is based on (or derived from) the Work and for which the editorial revisions, annotations, elaborations, or other modifications represent, as a whole, an original work of authorship. For the purposes of this License, Derivative Works shall not include works that remain separable from, or merely link (or bind by name) to the interfaces of, the Work and Derivative Works thereof.\n" +
            "\n" +
            "\"Contribution\" shall mean any work of authorship, including the original version of the Work and any modifications or additions to that Work or Derivative Works thereof, that is intentionally submitted to Licensor for inclusion in the Work by the copyright owner or by an individual or Legal Entity authorized to submit on behalf of the copyright owner. For the purposes of this definition, \"submitted\" means any form of electronic, verbal, or written communication sent to the Licensor or its representatives, including but not limited to communication on electronic mailing lists, source code control systems, and issue tracking systems that are managed by, or on behalf of, the Licensor for the purpose of discussing and improving the Work, but excluding communication that is conspicuously marked or otherwise designated in writing by the copyright owner as \"Not a Contribution.\"\n" +
            "\n" +
            "\"Contributor\" shall mean Licensor and any individual or Legal Entity on behalf of whom a Contribution has been received by Licensor and subsequently incorporated within the Work.\n" +
            "\n" +
            "2. Grant of Copyright License. Subject to the terms and conditions of this License, each Contributor hereby grants to You a perpetual, worldwide, non-exclusive, no-charge, royalty-free, irrevocable copyright license to reproduce, prepare Derivative Works of, publicly display, publicly perform, sublicense, and distribute the Work and such Derivative Works in Source or Object form.\n" +
            "\n" +
            "3. Grant of Patent License. Subject to the terms and conditions of this License, each Contributor hereby grants to You a perpetual, worldwide, non-exclusive, no-charge, royalty-free, irrevocable (except as stated in this section) patent license to make, have made, use, offer to sell, sell, import, and otherwise transfer the Work, where such license applies only to those patent claims licensable by such Contributor that are necessarily infringed by their Contribution(s) alone or by combination of their Contribution(s) with the Work to which such Contribution(s) was submitted. If You institute patent litigation against any entity (including a cross-claim or counterclaim in a lawsuit) alleging that the Work or a Contribution incorporated within the Work constitutes direct or contributory patent infringement, then any patent licenses granted to You under this License for that Work shall terminate as of the date such litigation is filed.\n" +
            "\n" +
            "4. Redistribution. You may reproduce and distribute copies of the Work or Derivative Works thereof in any medium, with or without modifications, and in Source or Object form, provided that You meet the following conditions:\n" +
            "\n" +
            "You must give any other recipients of the Work or Derivative Works a copy of this License; and\n" +
            "You must cause any modified files to carry prominent notices stating that You changed the files; and\n" +
            "You must retain, in the Source form of any Derivative Works that You distribute, all copyright, patent, trademark, and attribution notices from the Source form of the Work, excluding those notices that do not pertain to any part of the Derivative Works; and\n" +
            "If the Work includes a \"NOTICE\" text file as part of its distribution, then any Derivative Works that You distribute must include a readable copy of the attribution notices contained within such NOTICE file, excluding those notices that do not pertain to any part of the Derivative Works, in at least one of the following places: within a NOTICE text file distributed as part of the Derivative Works; within the Source form or documentation, if provided along with the Derivative Works; or, within a display generated by the Derivative Works, if and wherever such third-party notices normally appear. The contents of the NOTICE file are for informational purposes only and do not modify the License. You may add Your own attribution notices within Derivative Works that You distribute, alongside or as an addendum to the NOTICE text from the Work, provided that such additional attribution notices cannot be construed as modifying the License.\n" +
            "\n" +
            "You may add Your own copyright statement to Your modifications and may provide additional or different license terms and conditions for use, reproduction, or distribution of Your modifications, or for any such Derivative Works as a whole, provided Your use, reproduction, and distribution of the Work otherwise complies with the conditions stated in this License.\n" +
            "5. Submission of Contributions. Unless You explicitly state otherwise, any Contribution intentionally submitted for inclusion in the Work by You to the Licensor shall be under the terms and conditions of this License, without any additional terms or conditions. Notwithstanding the above, nothing herein shall supersede or modify the terms of any separate license agreement you may have executed with Licensor regarding such Contributions.\n" +
            "\n" +
            "6. Trademarks. This License does not grant permission to use the trade names, trademarks, service marks, or product names of the Licensor, except as required for reasonable and customary use in describing the origin of the Work and reproducing the content of the NOTICE file.\n" +
            "\n" +
            "7. Disclaimer of Warranty. Unless required by applicable law or agreed to in writing, Licensor provides the Work (and each Contributor provides its Contributions) on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied, including, without limitation, any warranties or conditions of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE. You are solely responsible for determining the appropriateness of using or redistributing the Work and assume any risks associated with Your exercise of permissions under this License.\n" +
            "\n" +
            "8. Limitation of Liability. In no event and under no legal theory, whether in tort (including negligence), contract, or otherwise, unless required by applicable law (such as deliberate and grossly negligent acts) or agreed to in writing, shall any Contributor be liable to You for damages, including any direct, indirect, special, incidental, or consequential damages of any character arising as a result of this License or out of the use or inability to use the Work (including but not limited to damages for loss of goodwill, work stoppage, computer failure or malfunction, or any and all other commercial damages or losses), even if such Contributor has been advised of the possibility of such damages.\n" +
            "\n" +
            "9. Accepting Warranty or Additional Liability. While redistributing the Work or Derivative Works thereof, You may choose to offer, and charge a fee for, acceptance of support, warranty, indemnity, or other liability obligations and/or rights consistent with this License. However, in accepting such obligations, You may act only on Your own behalf and on Your sole responsibility, not on behalf of any other Contributor, and only if You agree to indemnify, defend, and hold each Contributor harmless for any liability incurred by, or claims asserted against, such Contributor by reason of your accepting any such warranty or additional liability.\n" +
            "\n" +
            "END OF TERMS AND CONDITIONS"
        );
    }

    private void write_license_mozilla(JTextPane txt) {
        txt.setText(
            "Mozilla Public License Version 2.0\n" +
            "1. Definitions\n" +
            "1.1. \"Contributor\" means each individual or legal entity that creates, contributes to the creation of, or owns Covered Software.\n" +
            "\n" +
            "1.2. \"Contributor Version\" means the combination of the Contributions of others (if any) used by a Contributor and that particular Contributor's Contribution.\n" +
            "\n" +
            "1.3. \"Contribution\" means Covered Software of a particular Contributor.\n" +
            "\n" +
            "1.4. \"Covered Software\" means Source Code Form to which the initial Contributor has attached the notice in Exhibit A, the Executable Form of such Source Code Form, and Modifications of such Source Code Form, in each case including portions thereof.\n" +
            "\n" +
            "1.5. \"Incompatible With Secondary Licenses\" means\n" +
            "\n" +
            "a. that the initial Contributor has attached the notice described in Exhibit B to the Covered Software; or\n" +
            "\n" +
            "b. that the Covered Software was made available under the terms of version 1.1 or earlier of the License, but not also under the terms of a Secondary License.\n" +
            "\n" +
            "1.6. \"Executable Form\" means any form of the work other than Source Code Form.\n" +
            "\n" +
            "1.7. \"Larger Work\" means a work that combines Covered Software with other material, in a separate file or files, that is not Covered Software.\n" +
            "\n" +
            "1.8. \"License\" means this document.\n" +
            "\n" +
            "1.9. \"Licensable\" means having the right to grant, to the maximum extent possible, whether at the time of the initial grant or subsequently, any and all of the rights conveyed by this License.\n" +
            "\n" +
            "1.10. \"Modifications\" means any of the following:\n" +
            "\n" +
            "a. any file in Source Code Form that results from an addition to, deletion from, or modification of the contents of Covered Software; or\n" +
            "\n" +
            "b. any new file in Source Code Form that contains any Covered Software.\n" +
            "\n" +
            "1.11. \"Patent Claims\" of a Contributor means any patent claim(s), including without limitation, method, process, and apparatus claims, in any patent Licensable by such Contributor that would be infringed, but for the grant of the License, by the making, using, selling, offering for sale, having made, import, or transfer of either its Contributions or its Contributor Version.\n" +
            "\n" +
            "1.12. \"Secondary License\" means either the GNU General Public License, Version 2.0, the GNU Lesser General Public License, Version 2.1, the GNU Affero General Public License, Version 3.0, or any later versions of those licenses.\n" +
            "\n" +
            "1.13. \"Source Code Form\" means the form of the work preferred for making modifications.\n" +
            "\n" +
            "1.14. \"You\" (or \"Your\") means an individual or a legal entity exercising rights under this License. For legal entities, \"You\" includes any entity that controls, is controlled by, or is under common control with You. For purposes of this definition, \"control\" means (a) the power, direct or indirect, to cause the direction or management of such entity, whether by contract or otherwise, or (b) ownership of more than fifty percent (50%) of the outstanding shares or beneficial ownership of such entity.\n" +
            "\n" +
            "2. License Grants and Conditions\n" +
            "2.1. Grants\n" +
            "Each Contributor hereby grants You a world-wide, royalty-free, non-exclusive license:\n" +
            "\n" +
            "under intellectual property rights (other than patent or trademark) Licensable by such Contributor to use, reproduce, make available, modify, display, perform, distribute, and otherwise exploit its Contributions, either on an unmodified basis, with Modifications, or as part of a Larger Work; and\n" +
            "\n" +
            "under Patent Claims of such Contributor to make, use, sell, offer for sale, have made, import, and otherwise transfer either its Contributions or its Contributor Version.\n" +
            "\n" +
            "2.2. Effective Date\n" +
            "The licenses granted in Section 2.1 with respect to any Contribution become effective for each Contribution on the date the Contributor first distributes such Contribution.\n" +
            "\n" +
            "2.3. Limitations on Grant Scope\n" +
            "The licenses granted in this Section 2 are the only rights granted under this License. No additional rights or licenses will be implied from the distribution or licensing of Covered Software under this License. Notwithstanding Section 2.1(b) above, no patent license is granted by a Contributor:\n" +
            "\n" +
            "for any code that a Contributor has removed from Covered Software; or\n" +
            "\n" +
            "for infringements caused by: (i) Your and any other third party's modifications of Covered Software, or (ii) the combination of its Contributions with other software (except as part of its Contributor Version); or\n" +
            "\n" +
            "under Patent Claims infringed by Covered Software in the absence of its Contributions.\n" +
            "\n" +
            "This License does not grant any rights in the trademarks, service marks, or logos of any Contributor (except as may be necessary to comply with the notice requirements in Section 3.4).\n" +
            "\n" +
            "2.4. Subsequent Licenses\n" +
            "No Contributor makes additional grants as a result of Your choice to distribute the Covered Software under a subsequent version of this License (see Section 10.2) or under the terms of a Secondary License (if permitted under the terms of Section 3.3).\n" +
            "\n" +
            "2.5. Representation\n" +
            "Each Contributor represents that the Contributor believes its Contributions are its original creation(s) or it has sufficient rights to grant the rights to its Contributions conveyed by this License.\n" +
            "\n" +
            "2.6. Fair Use\n" +
            "This License is not intended to limit any rights You have under applicable copyright doctrines of fair use, fair dealing, or other equivalents.\n" +
            "\n" +
            "2.7. Conditions\n" +
            "Sections 3.1, 3.2, 3.3, and 3.4 are conditions of the licenses granted in Section 2.1.\n" +
            "\n" +
            "3. Responsibilities\n" +
            "3.1. Distribution of Source Form\n" +
            "All distribution of Covered Software in Source Code Form, including any Modifications that You create or to which You contribute, must be under the terms of this License. You must inform recipients that the Source Code Form of the Covered Software is governed by the terms of this License, and how they can obtain a copy of this License. You may not attempt to alter or restrict the recipients' rights in the Source Code Form.\n" +
            "\n" +
            "3.2. Distribution of Executable Form\n" +
            "If You distribute Covered Software in Executable Form then:\n" +
            "\n" +
            "such Covered Software must also be made available in Source Code Form, as described in Section 3.1, and You must inform recipients of the Executable Form how they can obtain a copy of such Source Code Form by reasonable means in a timely manner, at a charge no more than the cost of distribution to the recipient; and\n" +
            "\n" +
            "You may distribute such Executable Form under the terms of this License, or sublicense it under different terms, provided that the license for the Executable Form does not attempt to limit or alter the recipients' rights in the Source Code Form under this License.\n" +
            "\n" +
            "3.3. Distribution of a Larger Work\n" +
            "You may create and distribute a Larger Work under terms of Your choice, provided that You also comply with the requirements of this License for the Covered Software. If the Larger Work is a combination of Covered Software with a work governed by one or more Secondary Licenses, and the Covered Software is not Incompatible With Secondary Licenses, this License permits You to additionally distribute such Covered Software under the terms of such Secondary License(s), so that the recipient of the Larger Work may, at their option, further distribute the Covered Software under the terms of either this License or such Secondary License(s).\n" +
            "\n" +
            "3.4. Notices\n" +
            "You may not remove or alter the substance of any license notices (including copyright notices, patent notices, disclaimers of warranty, or limitations of liability) contained within the Source Code Form of the Covered Software, except that You may alter any license notices to the extent required to remedy known factual inaccuracies.\n" +
            "\n" +
            "3.5. Application of Additional Terms\n" +
            "You may choose to offer, and to charge a fee for, warranty, support, indemnity or liability obligations to one or more recipients of Covered Software. However, You may do so only on Your own behalf, and not on behalf of any Contributor. You must make it absolutely clear that any such warranty, support, indemnity, or liability obligation is offered by You alone, and You hereby agree to indemnify every Contributor for any liability incurred by such Contributor as a result of warranty, support, indemnity or liability terms You offer. You may include additional disclaimers of warranty and limitations of liability specific to any jurisdiction.\n" +
            "\n" +
            "4. Inability to Comply Due to Statute or Regulation\n" +
            "If it is impossible for You to comply with any of the terms of this License with respect to some or all of the Covered Software due to statute, judicial order, or regulation then You must: (a) comply with the terms of this License to the maximum extent possible; and (b) describe the limitations and the code they affect. Such description must be placed in a text file included with all distributions of the Covered Software under this License. Except to the extent prohibited by statute or regulation, such description must be sufficiently detailed for a recipient of ordinary skill to be able to understand it.\n" +
            "\n" +
            "5. Termination\n" +
            "5.1. The rights granted under this License will terminate automatically if You fail to comply with any of its terms. However, if You become compliant, then the rights granted under this License from a particular Contributor are reinstated (a) provisionally, unless and until such Contributor explicitly and finally terminates Your grants, and (b) on an ongoing basis, if such Contributor fails to notify You of the non-compliance by some reasonable means prior to 60 days after You have come back into compliance. Moreover, Your grants from a particular Contributor are reinstated on an ongoing basis if such Contributor notifies You of the non-compliance by some reasonable means, this is the first time You have received notice of non-compliance with this License from such Contributor, and You become compliant prior to 30 days after Your receipt of the notice.\n" +
            "\n" +
            "5.2. If You initiate litigation against any entity by asserting a patent infringement claim (excluding declaratory judgment actions, counter-claims, and cross-claims) alleging that a Contributor Version directly or indirectly infringes any patent, then the rights granted to You by any and all Contributors for the Covered Software under Section 2.1 of this License shall terminate.\n" +
            "\n" +
            "5.3. In the event of termination under Sections 5.1 or 5.2 above, all end user license agreements (excluding distributors and resellers) which have been validly granted by You or Your distributors under this License prior to termination shall survive termination.\n" +
            "\n" +
            "6. Disclaimer of Warranty\n" +
            "Covered Software is provided under this License on an \"as is\" basis, without warranty of any kind, either expressed, implied, or statutory, including, without limitation, warranties that the Covered Software is free of defects, merchantable, fit for a particular purpose or non-infringing. The entire risk as to the quality and performance of the Covered Software is with You. Should any Covered Software prove defective in any respect, You (not any Contributor) assume the cost of any necessary servicing, repair, or correction. This disclaimer of warranty constitutes an essential part of this License. No use of any Covered Software is authorized under this License except under this disclaimer.\n" +
            "\n" +
            "7. Limitation of Liability\n" +
            "Under no circumstances and under no legal theory, whether tort (including negligence), contract, or otherwise, shall any Contributor, or anyone who distributes Covered Software as permitted above, be liable to You for any direct, indirect, special, incidental, or consequential damages of any character including, without limitation, damages for lost profits, loss of goodwill, work stoppage, computer failure or malfunction, or any and all other commercial damages or losses, even if such party shall have been informed of the possibility of such damages. This limitation of liability shall not apply to liability for death or personal injury resulting from such party's negligence to the extent applicable law prohibits such limitation. Some jurisdictions do not allow the exclusion or limitation of incidental or consequential damages, so this exclusion and limitation may not apply to You.\n" +
            "\n" +
            "8. Litigation\n" +
            "Any litigation relating to this License may be brought only in the courts of a jurisdiction where the defendant maintains its principal place of business and such litigation shall be governed by laws of that jurisdiction, without reference to its conflict-of-law provisions. Nothing in this Section shall prevent a party's ability to bring cross-claims or counter-claims.\n" +
            "\n" +
            "9. Miscellaneous\n" +
            "This License represents the complete agreement concerning the subject matter hereof. If any provision of this License is held to be unenforceable, such provision shall be reformed only to the extent necessary to make it enforceable. Any law or regulation which provides that the language of a contract shall be construed against the drafter shall not be used to construe this License against a Contributor.\n" +
            "\n" +
            "10. Versions of the License\n" +
            "10.1. New Versions\n" +
            "Mozilla Foundation is the license steward. Except as provided in Section 10.3, no one other than the license steward has the right to modify or publish new versions of this License. Each version will be given a distinguishing version number.\n" +
            "\n" +
            "10.2. Effect of New Versions\n" +
            "You may distribute the Covered Software under the terms of the version of the License under which You originally received the Covered Software, or under the terms of any subsequent version published by the license steward.\n" +
            "\n" +
            "10.3. Modified Versions\n" +
            "If you create software not governed by this License, and you want to create a new license for such software, you may create and use a modified version of this License if you rename the license and remove any references to the name of the license steward (except to note that such modified license differs from this License).\n" +
            "\n" +
            "10.4. Distributing Source Code Form that is Incompatible With Secondary Licenses\n" +
            "If You choose to distribute Source Code Form that is Incompatible With Secondary Licenses under the terms of this version of the License, the notice described in Exhibit B of this License must be attached.\n" +
            "\n" +
            "Exhibit A - Source Code Form License Notice\n" +
            "This Source Code Form is subject to the terms of the Mozilla\n" +
            "Public License, v. 2.0. If a copy of the MPL was not distributed\n" +
            "with this file, you can obtain one at http://mozilla.org/MPL/2.0\n" +
            "If it is not possible or desirable to put the notice in a particular file, then You may include the notice in a location (such as a LICENSE file in a relevant directory) where a recipient would be likely to look for such a notice.\n" +
            "\n" +
            "You may add additional accurate notices of copyright ownership.\n" +
            "\n" +
            "Exhibit B - \"Incompatible With Secondary Licenses\" Notice\n" +
            "This Source Code Form is \"Incompatible With Secondary Licenses\",\n" +
            "as defined by the Mozilla Public License, v. 2.0.\n" +
            "Eclipse Public License - Version 1.0\n" +
            "THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC LICENSE (\"AGREEMENT\"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.\n" +
            "\n" +
            "1. DEFINITIONS\n" +
            "\"Contribution\" means:\n" +
            "\n" +
            "a) in the case of the initial Contributor, the initial code and documentation distributed under this Agreement, and\n" +
            "\n" +
            "b) in the case of each subsequent Contributor:\n" +
            "\n" +
            "i) changes to the Program, and\n" +
            "\n" +
            "ii) additions to the Program;\n" +
            "\n" +
            "where such changes and/or additions to the Program originate from and are distributed by that particular Contributor. A Contribution 'originates' from a Contributor if it was added to the Program by such Contributor itself or anyone acting on such Contributor's behalf. Contributions do not include additions to the Program which: (i) are separate modules of software distributed in conjunction with the Program under their own license agreement, and (ii) are not derivative works of the Program.\n" +
            "\n" +
            "\"Contributor\" means any person or entity that distributes the Program.\n" +
            "\n" +
            "\"Licensed Patents \" mean patent claims licensable by a Contributor which are necessarily infringed by the use or sale of its Contribution alone or when combined with the Program.\n" +
            "\n" +
            "\"Program\" means the Contributions distributed in accordance with this Agreement.\n" +
            "\n" +
            "\"Recipient\" means anyone who receives the Program under this Agreement, including all Contributors.\n" +
            "\n" +
            "2. GRANT OF RIGHTS\n" +
            "a) Subject to the terms of this Agreement, each Contributor hereby grants Recipient a non-exclusive, worldwide, royalty-free copyright license to reproduce, prepare derivative works of, publicly display, publicly perform, distribute and sublicense the Contribution of such Contributor, if any, and such derivative works, in source code and object code form.\n" +
            "\n" +
            "b) Subject to the terms of this Agreement, each Contributor hereby grants Recipient a non-exclusive, worldwide, royalty-free patent license under Licensed Patents to make, use, sell, offer to sell, import and otherwise transfer the Contribution of such Contributor, if any, in source code and object code form. This patent license shall apply to the combination of the Contribution and the Program if, at the time the Contribution is added by the Contributor, such addition of the Contribution causes such combination to be covered by the Licensed Patents. The patent license shall not apply to any other combinations which include the Contribution. No hardware per se is licensed hereunder.\n" +
            "\n" +
            "c) Recipient understands that although each Contributor grants the licenses to its Contributions set forth herein, no assurances are provided by any Contributor that the Program does not infringe the patent or other intellectual property rights of any other entity. Each Contributor disclaims any liability to Recipient for claims brought by any other entity based on infringement of intellectual property rights or otherwise. As a condition to exercising the rights and licenses granted hereunder, each Recipient hereby assumes sole responsibility to secure any other intellectual property rights needed, if any. For example, if a third party patent license is required to allow Recipient to distribute the Program, it is Recipient's responsibility to acquire that license before distributing the Program.\n" +
            "\n" +
            "d) Each Contributor represents that to its knowledge it has sufficient copyright rights in its Contribution, if any, to grant the copyright license set forth in this Agreement.\n" +
            "\n" +
            "3. REQUIREMENTS\n" +
            "A Contributor may choose to distribute the Program in object code form under its own license agreement, provided that:\n" +
            "\n" +
            "a) it complies with the terms and conditions of this Agreement; and\n" +
            "\n" +
            "b) its license agreement:\n" +
            "\n" +
            "i) effectively disclaims on behalf of all Contributors all warranties and conditions, express and implied, including warranties or conditions of title and non-infringement, and implied warranties or conditions of merchantability and fitness for a particular purpose;\n" +
            "\n" +
            "ii) effectively excludes on behalf of all Contributors all liability for damages, including direct, indirect, special, incidental and consequential damages, such as lost profits;\n" +
            "\n" +
            "iii) states that any provisions which differ from this Agreement are offered by that Contributor alone and not by any other party; and\n" +
            "\n" +
            "iv) states that source code for the Program is available from such Contributor, and informs licensees how to obtain it in a reasonable manner on or through a medium customarily used for software exchange.\n" +
            "\n" +
            "When the Program is made available in source code form:\n" +
            "\n" +
            "a) it must be made available under this Agreement; and\n" +
            "\n" +
            "b) a copy of this Agreement must be included with each copy of the Program.\n" +
            "\n" +
            "Contributors may not remove or alter any copyright notices contained within the Program.\n" +
            "\n" +
            "Each Contributor must identify itself as the originator of its Contribution, if any, in a manner that reasonably allows subsequent Recipients to identify the originator of the Contribution.\n" +
            "\n" +
            "4. COMMERCIAL DISTRIBUTION\n" +
            "Commercial distributors of software may accept certain responsibilities with respect to end users, business partners and the like. While this license is intended to facilitate the commercial use of the Program, the Contributor who includes the Program in a commercial product offering should do so in a manner which does not create potential liability for other Contributors. Therefore, if a Contributor includes the Program in a commercial product offering, such Contributor (\"Commercial Contributor\") hereby agrees to defend and indemnify every other Contributor (\"Indemnified Contributor\") against any losses, damages and costs (collectively \"Losses\") arising from claims, lawsuits and other legal actions brought by a third party against the Indemnified Contributor to the extent caused by the acts or omissions of such Commercial Contributor in connection with its distribution of the Program in a commercial product offering. The obligations in this section do not apply to any claims or Losses relating to any actual or alleged intellectual property infringement. In order to qualify, an Indemnified Contributor must: a) promptly notify the Commercial Contributor in writing of such claim, and b) allow the Commercial Contributor to control, and cooperate with the Commercial Contributor in, the defense and any related settlement negotiations. The Indemnified Contributor may participate in any such claim at its own expense.\n" +
            "\n" +
            "For example, a Contributor might include the Program in a commercial product offering, Product X. That Contributor is then a Commercial Contributor. If that Commercial Contributor then makes performance claims, or offers warranties related to Product X, those performance claims and warranties are such Commercial Contributor's responsibility alone. Under this section, the Commercial Contributor would have to defend claims against the other Contributors related to those performance claims and warranties, and if a court requires any other Contributor to pay any damages as a result, the Commercial Contributor must pay those damages.\n" +
            "\n" +
            "5. NO WARRANTY\n" +
            "EXCEPT AS EXPRESSLY SET FORTH IN THIS AGREEMENT, THE PROGRAM IS PROVIDED ON AN \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED INCLUDING, WITHOUT LIMITATION, ANY WARRANTIES OR CONDITIONS OF TITLE, NON-INFRINGEMENT, MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Each Recipient is solely responsible for determining the appropriateness of using and distributing the Program and assumes all risks associated with its exercise of rights under this Agreement, including but not limited to the risks and costs of program errors, compliance with applicable laws, damage to or loss of data, programs or equipment, and unavailability or interruption of operations.\n" +
            "\n" +
            "6. DISCLAIMER OF LIABILITY\n" +
            "EXCEPT AS EXPRESSLY SET FORTH IN THIS AGREEMENT, NEITHER RECIPIENT NOR ANY CONTRIBUTORS SHALL HAVE ANY LIABILITY FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING WITHOUT LIMITATION LOST PROFITS), HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OR DISTRIBUTION OF THE PROGRAM OR THE EXERCISE OF ANY RIGHTS GRANTED HEREUNDER, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.\n" +
            "\n" +
            "7. GENERAL\n" +
            "If any provision of this Agreement is invalid or unenforceable under applicable law, it shall not affect the validity or enforceability of the remainder of the terms of this Agreement, and without further action by the parties hereto, such provision shall be reformed to the minimum extent necessary to make such provision valid and enforceable.\n" +
            "\n" +
            "If Recipient institutes patent litigation against any entity (including a cross-claim or counterclaim in a lawsuit) alleging that the Program itself (excluding combinations of the Program with other software or hardware) infringes such Recipient's patent(s), then such Recipient's rights granted under Section 2(b) shall terminate as of the date such litigation is filed.\n" +
            "\n" +
            "All Recipient's rights under this Agreement shall terminate if it fails to comply with any of the material terms or conditions of this Agreement and does not cure such failure in a reasonable period of time after becoming aware of such noncompliance. If all Recipient's rights under this Agreement terminate, Recipient agrees to cease use and distribution of the Program as soon as reasonably practicable. However, Recipient's obligations under this Agreement and any licenses granted by Recipient relating to the Program shall continue and survive.\n" +
            "\n" +
            "Everyone is permitted to copy and distribute copies of this Agreement, but in order to avoid inconsistency the Agreement is copyrighted and may only be modified in the following manner. The Agreement Steward reserves the right to publish new versions (including revisions) of this Agreement from time to time. No one other than the Agreement Steward has the right to modify this Agreement. The Eclipse Foundation is the initial Agreement Steward. The Eclipse Foundation may assign the responsibility to serve as the Agreement Steward to a suitable separate entity. Each new version of the Agreement will be given a distinguishing version number. The Program (including Contributions) may always be distributed subject to the version of the Agreement under which it was received. In addition, after a new version of the Agreement is published, Contributor may elect to distribute the Program (including its Contributions) under the new version. Except as expressly stated in Sections 2(a) and 2(b) above, Recipient receives no rights or licenses to the intellectual property of any Contributor under this Agreement, whether expressly, by implication, estoppel or otherwise. All rights in the Program not expressly granted under this Agreement are reserved.\n" +
            "\n" +
            "This Agreement is governed by the laws of the State of New York and the intellectual property laws of the United States of America. No party to this Agreement will bring a legal action under this Agreement more than one year after the cause of action arose. Each party waives its rights to a jury trial in any resulting litigation.\n" +
            "\n" +
            "Export Control Classification Number (ECCN)\n" +
            "As far as we know, the U.S. Export Control Classification Number (ECCN) for this software is 5D002. However, for legal reasons, we can make no warranty that this information is correct. For details, see also the Apache Software Foundation Export Classifications page."
        );
    }

    private void write_license_mit(JTextPane txt) {
        txt.setText(
            "MIT Lizenz\n" +
            "\n" +
            "Copyright (c) <2020> <Heiko Radde>\n" +
            "Jedem, der eine Kopie dieser Software und der zugehörigen Dokumentationsdateien (die \"Software\") erhält, wird hiermit kostenlos die Erlaubnis erteilt, ohne Einschränkung mit der Software zu handeln, einschließlich und ohne Einschränkung der Rechte zur Nutzung, zum Kopieren, Ändern, Zusammenführen, Veröffentlichen, Verteilen, Unterlizenzieren und/oder Verkaufen von Kopien der Software, und Personen, denen die Software zur Verfügung gestellt wird, dies unter den folgenden Bedingungen zu gestatten:\n" +
            "\n" +
            "Der obige Urheberrechtshinweis und dieser Genehmigungshinweis müssen in allen Kopien oder wesentlichen Teilen der Software enthalten sein.\n" +
            "\n" +
            "DIE SOFTWARE WIRD OHNE MÄNGELGEWÄHR UND OHNE JEGLICHE AUSDRÜCKLICHE ODER STILLSCHWEIGENDE GEWÄHRLEISTUNG, EINSCHLIEẞLICH, ABER NICHT BESCHRÄNKT AUF DIE GEWÄHRLEISTUNG DER MARKTGÄNGIGKEIT, DER EIGNUNG FÜR EINEN BESTIMMTEN ZWECK UND DER NICHTVERLETZUNG VON RECHTEN DRITTER, ZUR VERFÜGUNG GESTELLT. DIE AUTOREN ODER URHEBERRECHTSINHABER SIND IN KEINEM FALL HAFTBAR FÜR ANSPRÜCHE, SCHÄDEN ODER ANDERE VERPFLICHTUNGEN, OB IN EINER VERTRAGS- ODER HAFTUNGSKLAGE, EINER UNERLAUBTEN HANDLUNG ODER ANDERWEITIG, DIE SICH AUS, AUS ODER IN VERBINDUNG MIT DER SOFTWARE ODER DER NUTZUNG ODER ANDEREN GESCHÄFTEN MIT DER SOFTWARE ERGEBEN."
        );
    }
}
