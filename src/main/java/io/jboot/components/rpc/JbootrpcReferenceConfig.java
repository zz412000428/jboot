/**
 * Copyright (c) 2015-2020, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jboot.components.rpc;

import io.jboot.components.rpc.annotation.RPCInject;

import java.io.Serializable;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jboot.components.rpc
 */
public class JbootrpcReferenceConfig implements Serializable {

    /**
     * Service version, default value is empty string
     */
    String version;

    /**
     * Service group, default value is empty string
     */
    String group;

    /**
     * Service target URL for direct invocation, if this is specified, then registry center takes no effect.
     */
    String url;


    /**
     * Whether to enable generic invocation, default value is false
     */
    boolean generic;


    /**
     * Check if service provider is available during boot up, default value is true
     */
    boolean check;


    /**
     * Service invocation retry times
     * <p>
     * see Constants#DEFAULT_RETRIES
     */
    int retries;


    /**
     * Load balance strategy, legal values include: random, roundrobin, leastactive
     * <p>
     * see Constants#DEFAULT_LOADBALANCE
     */
    String loadbalance;

    /**
     * Whether to enable async invocation, default value is false
     */
    boolean async;

    /**
     * Maximum active requests allowed, default value is 0
     */
    int actives;


    /**
     * Timeout value for service invocation, default value is 0
     */
    int timeout;

    /**
     * Application associated name
     */
    String application;

    /**
     * Module associated name
     */
    String module;


    /**
     * Consumer associated name
     */
    String consumer;

    /**
     * Monitor associated name
     */
    String monitor;

    /**
     * Registry associated name
     */
    String registry;

    /**
     *
     * @return the default value is ""
     */
    String protocol;

    /**
     * Service tag name
     */
    String tag;

    /**
     * The id
     *
     * @return default value is empty
     */
    String id;


    public JbootrpcReferenceConfig() {
    }

    public JbootrpcReferenceConfig(RPCInject inject) {
        Utils.appendAnnotation(RPCInject.class, inject, this);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isGeneric() {
        return generic;
    }

    public void setGeneric(boolean generic) {
        this.generic = generic;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public String getLoadbalance() {
        return loadbalance;
    }

    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public int getActives() {
        return actives;
    }

    public void setActives(int actives) {
        this.actives = actives;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
