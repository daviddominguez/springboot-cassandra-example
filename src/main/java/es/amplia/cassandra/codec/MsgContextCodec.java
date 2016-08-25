package es.amplia.cassandra.codec;

import com.datastax.driver.core.TypeCodec;

import java.util.LinkedHashMap;
import java.util.Map;

public class MsgContextCodec extends TypeCodec.AbstractMapCodec<String, String> {

        public MsgContextCodec() {
            super(TypeCodec.varchar(), TypeCodec.varchar());
        }

        @Override
        protected Map<String, String> newInstance(int size) {
            return new LinkedHashMap<>(size);
        }

    }