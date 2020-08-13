package appengine.parser.temp;

import appengine.parser.mysqlmodels.tables.Swiggyevents;
import appengine.parser.utils.DataBaseConnector;
import com.google.gson.Gson;
import netscape.javascript.JSObject;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Result;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SwiggyEventsUtil {

    public void addAll(List<SwiggyEventAndHeader> swiggyEventAndHeaderList) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();

        if (swiggyEventAndHeaderList == null || swiggyEventAndHeaderList.size() == 0) {
            return;
        }


        for (SwiggyEventAndHeader swiggyEventAndHeader : swiggyEventAndHeaderList) {
            dslContext.insertInto(Swiggyevents.SWIGGYEVENTS,
                    Swiggyevents.SWIGGYEVENTS.JSON, Swiggyevents.SWIGGYEVENTS.DEVICE_ID).values(
                    swiggyEventAndHeader.toJSON(), swiggyEventAndHeader.event.device_id)
                    .execute();
        }
    }


    public void addAll(SwiggyEventList swiggyEventList) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();

        if (swiggyEventList.events == null || swiggyEventList.events.size() == 0) {
            return;
        }
        for (SwiggyEvent swiggyEvent : swiggyEventList.events) {
            dslContext.insertInto(Swiggyevents.SWIGGYEVENTS,
                    Swiggyevents.SWIGGYEVENTS.JSON, Swiggyevents.SWIGGYEVENTS.DEVICE_ID).values(
                    swiggyEvent.toJSON(), swiggyEvent.device_id)
                    .execute();
        }
    }

    public void add(SwiggyEvent swiggyEvent) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();

        dslContext.insertInto(Swiggyevents.SWIGGYEVENTS,
                Swiggyevents.SWIGGYEVENTS.JSON, Swiggyevents.SWIGGYEVENTS.DEVICE_ID).values(
                swiggyEvent.toJSON(), swiggyEvent.device_id)
                .execute();
    }

    public void deleteAll() {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.deleteFrom(Swiggyevents.SWIGGYEVENTS).execute();
    }

    public void deleteByDevice(String deviceId) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.deleteFrom(Swiggyevents.SWIGGYEVENTS).where(Swiggyevents.SWIGGYEVENTS.DEVICE_ID.eq(deviceId)).execute();
    }

    public JSONObject getEvents(int limit) {


        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record2<Integer, String>> result = dslContext.select(Swiggyevents.SWIGGYEVENTS.ID,
                Swiggyevents.SWIGGYEVENTS.JSON).from(Swiggyevents.SWIGGYEVENTS).limit(limit).fetch();

        SwiggyEventList swiggyEventList = new SwiggyEventList();
        swiggyEventList.events = new ArrayList<>();

        for (Record2<Integer, String> record2 : result) {
            swiggyEventList.events.add(new Gson().fromJson(record2.value2(), SwiggyEvent.class));
        }


        return new JSONObject(swiggyEventList.toJSON());
    }

    public JSONArray getSwiggyEventsAndHeaders(int limit) {

        return getSwiggyEventsAndHeaders(limit, null);

    }

    public JSONArray getSwiggyEventsAndHeaders(int limit, String deviceId) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record2<Integer, String>> result;

        if (deviceId == null) {
            result = dslContext.select(Swiggyevents.SWIGGYEVENTS.ID,
                    Swiggyevents.SWIGGYEVENTS.JSON).from(Swiggyevents.SWIGGYEVENTS).limit(limit).fetch();
        } else {
            result = dslContext.select(Swiggyevents.SWIGGYEVENTS.ID,
                    Swiggyevents.SWIGGYEVENTS.JSON).from(Swiggyevents.SWIGGYEVENTS).where(Swiggyevents.SWIGGYEVENTS.DEVICE_ID.eq(
                    deviceId)).limit(limit).fetch();
        }

        List<SwiggyEventAndHeader> swiggyEventList = new ArrayList<>();

        for (Record2<Integer, String> record2 : result) {
            swiggyEventList.add(new Gson().fromJson(record2.value2(), SwiggyEventAndHeader.class));
        }

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < swiggyEventList.size(); i++) {
            jsonArray.put(new JSONObject(swiggyEventList.get(i).toJSON()));
        }

        return jsonArray;

    }

    public JSONObject getSwiggyEventsAndHeadersCount(String deviceId) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record1<Integer>> result;

            result = dslContext.selectCount().from(Swiggyevents.SWIGGYEVENTS).where(Swiggyevents.SWIGGYEVENTS.DEVICE_ID.eq(
                    deviceId)).fetch();


        JSONObject jsObject = new JSONObject();
        jsObject.put("count",result.get(0).value1());


        return jsObject;

    }
}
