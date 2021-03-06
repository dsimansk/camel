/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.aws.ec2;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.ScheduledPollEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.util.ObjectHelper;

/**
 * Defines the <a href="http://camel.apache.org/aws.html">AWS EC2 Endpoint</a>.  
 */
@UriEndpoint(scheme = "aws-ec2", title = "AWS EC2", syntax = "aws-ec2:label", producerOnly = true, label = "cloud,management")
public class EC2Endpoint extends ScheduledPollEndpoint {
    
    private AmazonEC2Client ec2Client;

    @UriParam
    private EC2Configuration configuration;
    
    public EC2Endpoint(String uri, Component component, EC2Configuration configuration) {
        super(uri, component);
        this.configuration = configuration;
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("You cannot receive messages from this endpoint");
    }

    public Producer createProducer() throws Exception {
        return new EC2Producer(this);
    }

    public boolean isSingleton() {
        return true;
    }

    @Override
    public void doStart() throws Exception {
        super.doStart();
        
        ec2Client = configuration.getAmazonEc2Client() != null ? configuration.getAmazonEc2Client() : createEc2Client();
        if (ObjectHelper.isNotEmpty(configuration.getAmazonEc2Endpoint())) {
            ec2Client.setEndpoint(configuration.getAmazonEc2Endpoint());
        }
    }

    public EC2Configuration getConfiguration() {
        return configuration;
    }

    public AmazonEC2Client getEc2Client() {
        return ec2Client;
    }

    AmazonEC2Client createEc2Client() {
        AWSCredentials credentials = new BasicAWSCredentials(configuration.getAccessKey(), configuration.getSecretKey());
        AmazonEC2Client client = new AmazonEC2Client(credentials);
        return client;
    }
}
