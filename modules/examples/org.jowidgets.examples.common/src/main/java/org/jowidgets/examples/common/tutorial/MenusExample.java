/*
 * Copyright (c) 2013, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.examples.common.tutorial;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IActionMenuItem;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IMainMenu;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.ISelectableMenuItem;
import org.jowidgets.api.widgets.ISubMenu;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IItemStateListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class MenusExample implements IApplication {

    @Override
    public void start(final IApplicationLifecycle lifecycle) {

        //Create a frame BluePrint with help of the BluePrintFactory (BPF)
        final IFrameBluePrint frameBp = BPF.frame();
        frameBp.setSize(new Dimension(400, 300)).setTitle("Hello World");

        //Create a frame with help of the Toolkit and BluePrint. This convenience
        //method finishes the ApplicationLifecycle when the root frame will be closed.
        final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);

        //Use a simple MigLayout with one column and one row for the frame (a frame is a container also)
        frame.setLayout(new MigLayoutDescriptor("[]", "[]"));

        //Create a button BluePrint with help of the BluePrintFactory (BPF)
        final IButtonBluePrint buttonBp = BPF.button().setText("Hello World");

        //Add the button defined by the BluePrint to the frame
        final IButton button = frame.add(buttonBp);

        //Add an ActionListener to the button
        button.addActionListener(new IActionListener() {
            @Override
            public void actionPerformed() {
                //CHECKSTYLE:OFF
                System.out.println("Hello World");
                //CHECKSTYLE:ON
            }
        });

        //****************************************************************
        //MENU EXAMPLE
        //****************************************************************
        final IMenuBar menuBar = frame.createMenuBar();

        final IMainMenu mainMenu = menuBar.addMenu(BPF.mainMenu("Menu1"));

        final IActionMenuItem actionItem = mainMenu.addItem(BPF.menuItem("ActionItem"));
        actionItem.addActionListener(new IActionListener() {
            @Override
            public void actionPerformed() {
                //CHECKSTYLE:OFF
                System.out.println("Action Performed");
                //CHECKSTYLE:ON
            }
        });

        final ISelectableMenuItem selectableItem = mainMenu.addItem(BPF.checkedMenuItem("Checked Item"));
        selectableItem.setSelected(true);
        selectableItem.addItemListener(new IItemStateListener() {
            @Override
            public void itemStateChanged() {
                //CHECKSTYLE:OFF
                System.out.println(selectableItem.isSelected());
                //CHECKSTYLE:ON
            }
        });

        mainMenu.addSeparator();

        final ISubMenu subMenu = mainMenu.addItem(BPF.subMenu("SubMenu"));
        final ISelectableMenuItem radio1 = subMenu.addItem(BPF.radioMenuItem("Radio1"));
        subMenu.addItem(BPF.radioMenuItem("Radio2").setSelected(true));
        subMenu.addItem(BPF.radioMenuItem("Radio3"));

        radio1.addItemListener(new IItemStateListener() {
            @Override
            public void itemStateChanged() {
                //CHECKSTYLE:OFF
                System.out.println(radio1.isSelected());
                //CHECKSTYLE:ON
            }
        });

        //set the root frame visible
        frame.setVisible(true);
    }

}
