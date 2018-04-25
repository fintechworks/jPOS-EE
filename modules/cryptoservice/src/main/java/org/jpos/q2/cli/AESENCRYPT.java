/*
 * jPOS Project [http://jpos.org]
 * Copyright (C) 2000-2018 jPOS Software SRL
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jpos.q2.cli;

import org.jpos.crypto.CryptoService;
import org.jpos.crypto.SecureData;
import org.jpos.iso.ISOUtil;
import org.jpos.q2.CLICommand;
import org.jpos.q2.CLIContext;
import org.jpos.util.NameRegistrar;

import javax.crypto.spec.IvParameterSpec;
import java.nio.ByteBuffer;

public class AESENCRYPT implements CLICommand {
    CryptoService cs;

    @Override
    public void exec(CLIContext cli, String[] args) throws Exception {
        cs = (CryptoService) NameRegistrar.getIfExists("crypto-service");
        if (args.length != 2) {
            usage(cli);
            if (cs == null)
                cli.println ("'crypto-service' not registered");
            return;
        }
        encrypt(cli, args[1]);
    }

    private void usage (CLIContext cli) {
        cli.println ("Usage: AESENCRYPT clear-text");
    }

    private void encrypt (CLIContext cli, String clear) throws Exception {
        SecureData sd = cs.aesEncrypt(clear.getBytes());
        cli.println (sd.getId() + " " + ISOUtil.hexString(sd.getEncoded()));
    }
}