module org.karabela.totp {
    requires org.apache.commons.codec;
    requires com.google.zxing;
    requires com.google.zxing.javase;

    requires static org.apache.commons.net;

    exports org.karabela.totp;
    exports org.karabela.totp.code;
    exports org.karabela.totp.exceptions;
    exports org.karabela.totp.qr;
    exports org.karabela.totp.recovery;
    exports org.karabela.totp.secret;
    exports org.karabela.totp.time;
    exports org.karabela.totp.util;
}
