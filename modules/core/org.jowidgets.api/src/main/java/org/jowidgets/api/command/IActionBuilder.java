/*
 * Copyright (c) 2010, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.api.command;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;

public interface IActionBuilder {

    /**
     * Sets the actions label text
     * 
     * @param text The text to set
     * 
     * @return This builder
     */
    IActionBuilder setText(String text);

    /**
     * Sets the actions tooltip text
     * 
     * @param toolTipText The text to set
     * 
     * @return This builder
     */
    IActionBuilder setToolTipText(String toolTipText);

    /**
     * Sets the actions icon
     * 
     * @param icon The icon to set
     * 
     * @return This builder
     */
    IActionBuilder setIcon(IImageConstant icon);

    /**
     * Sets the actions mnemonic
     * 
     * @param mnemonic The mnemonic to set, may be null
     * 
     * @return This builder
     */
    IActionBuilder setMnemonic(Character mnemonic);

    /**
     * Sets the actions mnemonic
     * 
     * @param mnemonic The mnemonic to set
     * 
     * @return This builder
     */
    IActionBuilder setMnemonic(char mnemonic);

    /**
     * Sets the actions accelerator
     * 
     * @param mnemonic The accelerator to set
     * 
     * @return This builder
     */
    IActionBuilder setAccelerator(Accelerator accelerator);

    /**
     * Sets the actions accelerator
     * 
     * @param key The accelerator key
     * @param modifier The accelerator modifier
     * 
     * @return This builder
     */
    IActionBuilder setAccelerator(char key, Modifier... modifier);

    /**
     * Sets the actions accelerator
     * 
     * @param virtualKey The accelerator key
     * @param modifier The accelerator modifier
     * 
     * @return This builder
     */
    IActionBuilder setAccelerator(VirtualKey virtualKey, Modifier... modifier);

    /**
     * Sets the enabled state of the action
     * 
     * @param enabled The enabled state to set
     * 
     * @return This builder
     */
    IActionBuilder setEnabled(boolean enabled);

    /**
     * Sets the actions command
     * 
     * @param command The command to set, may be null if the action should not have a command
     * 
     * @return This builder
     */
    IActionBuilder setCommand(ICommand command);

    /**
     * Sets the actions command defined by a command executor
     * 
     * The command that will be set has no enabled checker and no exception handler
     * 
     * @param command The executor of the command to set
     * 
     * @return This builder
     */
    IActionBuilder setCommand(ICommandExecutor command);

    /**
     * Sets the actions command defined by a command executor and enabled checker
     * 
     * The command that will be set has no exception handler
     * 
     * @param command The executor of the command to set
     * @param enabledChecker The enabled checker of the command to set
     * 
     * @return This builder
     */
    IActionBuilder setCommand(ICommandExecutor command, IEnabledChecker executableStateChecker);

    /**
     * Sets the actions command defined by a command executor and exception handler
     * 
     * The command that will be set has no enabled checker
     * 
     * @param command The executor of the command to set
     * @param exceptionHandler The exception handler of the command to set
     * 
     * @return This builder
     */
    IActionBuilder setCommand(ICommandExecutor command, IExceptionHandler exceptionHandler);

    /**
     * Sets the actions command defined by a command executor, enabled checker and exception handler
     * 
     * @param command The executor of the command to set
     * @param enabledChecker The enabled checker of the command to set
     * @param exceptionHandler The exception handler of the command to set
     * 
     * @return This builder
     */
    IActionBuilder setCommand(ICommandExecutor command, IEnabledChecker enabledChecker, IExceptionHandler exceptionHandler);

    /**
     * Set's the ExceptionHandler of the action. The actions ExceptionHandler handles exceptions that are not
     * handled by the command's exception handler.
     * 
     * The actions ExceptionHandler should be implemented independently of the current command. If exception handling
     * is command specific, the commands exception handler should be used for that.
     * 
     * If no exception handler is set, a default handler will be used for the action.
     * 
     * @param exceptionHandler The ExceptionHandler to set
     * 
     * @return This builder
     */
    IActionBuilder setActionExceptionHandler(IExceptionHandler exceptionHandler);

    /**
     * Creates a new command action defined by the builder setup
     * 
     * @return A new command action, never null
     */
    ICommandAction build();

}
