package com.edavalos.mtx.mailjet.transport;

import com.edavalos.mtx.mailjet.MtxEmail;

public interface MtxEmailTransport {

    void send(MtxEmail email);
}
