/*
 * Copyright (c) 2013, MGrossmann
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

package org.jowidgets.impl.toolkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jowidgets.api.animation.IAnimationRunner;
import org.jowidgets.api.animation.IAnimationStep;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.util.Assert;
import org.jowidgets.util.ICallback;
import org.jowidgets.util.Tuple;

final class AnimationRunnerImpl implements IAnimationRunner {

    private final IUiThreadAccess uiThreadAccess;
    private final Queue<Tuple<Runnable, ICallback<Void>>> events;
    private final ScheduledExecutorService executorService;

    private long delay;
    private TimeUnit timeUnit;
    private ScheduledFuture<?> scheduledFuture;

    AnimationRunnerImpl(final ScheduledExecutorService executorService, final long delay, final TimeUnit timeUnit) {
        Assert.paramNotNull(timeUnit, "timeUnit");
        this.delay = delay;
        this.timeUnit = timeUnit;
        this.events = new ConcurrentLinkedQueue<Tuple<Runnable, ICallback<Void>>>();
        this.uiThreadAccess = Toolkit.getUiThreadAccess();
        this.executorService = executorService;
    }

    @Override
    public void setDelay(final long delay) {
        setDelay(delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public synchronized void setDelay(final long delay, final TimeUnit timeUnit) {
        Assert.paramNotNull(timeUnit, "timeUnit");

        //only change, of delay relay has been changed
        if (delay != this.delay || timeUnit != this.timeUnit) {

            this.delay = delay;
            this.timeUnit = timeUnit;

            if (scheduledFuture != null) {
                //stop but do not clear events and restart
                scheduledFuture.cancel(false);
                scheduledFuture = null;
                start();
            }
        }

    }

    @Override
    public synchronized void start() {
        if (scheduledFuture == null) {
            scheduledFuture = executorService.scheduleWithFixedDelay(new EventDispatcher(), delay, delay, timeUnit);
        }
    }

    @Override
    public synchronized void stop() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledFuture = null;
        }
        events.clear();
    }

    @Override
    public boolean isRunning() {
        return scheduledFuture != null;
    }

    @Override
    public void run(final Runnable animationStep, final ICallback<Void> callback) {
        Assert.paramNotNull(animationStep, "animationStep");
        events.add(new Tuple<Runnable, ICallback<Void>>(animationStep, callback));
    }

    @Override
    public void run(final Runnable animationStep) {
        run(animationStep, null);
    }

    @Override
    public void run(final IAnimationStep animationStep) {
        Assert.paramNotNull(animationStep, "animationStep");
        run(animationStep.getAnimationStep(), animationStep.getFinishedCallback());
    }

    private final class EventDispatcher implements Runnable {

        @Override
        public void run() {
            if (scheduledFuture == null) {
                return;
            }

            final int eventsSize = events.size();

            if (eventsSize > 0) {
                final List<Tuple<Runnable, ICallback<Void>>> currentEvents = new ArrayList<Tuple<Runnable, ICallback<Void>>>(
                    eventsSize);
                for (int i = 0; i < eventsSize; i++) {
                    final Tuple<Runnable, ICallback<Void>> event = events.poll();
                    if (event != null) {
                        currentEvents.add(event);
                    }
                    else {
                        break;
                    }
                }

                for (final Tuple<Runnable, ICallback<Void>> event : currentEvents) {
                    if (scheduledFuture == null) {
                        return;
                    }
                    uiThreadAccess.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (scheduledFuture == null) {
                                return;
                            }
                            event.getFirst().run();
                            final ICallback<Void> callback = event.getSecond();
                            if (callback != null) {
                                callback.call(null);
                            }
                        }
                    });
                }

            }
        }
    }

}
