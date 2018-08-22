package com.crunchify.tutorials;
 
import java.io.FileWriter;
import java.io.IOException;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
 
/**
 * @author Crunchify.com
 */
 
public class CrunchifyJSONFileWrite {
 
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
 
		JSONObject obj = new JSONObject();
		obj.put("Name", "crunchify.com");
		obj.put("Author", "App Shah");
 
		JSONArray company = new JSONArray();
		company.add("Compnay: eBay");
		company.add("Compnay: Paypal");
		company.add("Compnay: Google");
		obj.put("Company List", company);
 
		// try-with-resources statement based on post comment below :)
		try (FileWriter file = new FileWriter("/Users/<username>/Documents/file1.txt")) {
			file.write(obj.toJSONString());
			System.out.println("Successfully Copied JSON Object to File...");
			System.out.println("\nJSON Object: " + obj);
		}
	}
}



// Output

Successfully Copied JSON Object to File...
 
JSON Object: {"Name":"crunchify.com","Author":"App Shah","Company List":["Compnay: eBay","Compnay: Paypal","Compnay: Google"]}


public class CoveoJSONFileWrite {
 
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
 
        // ----- JSON Object/Array Creation -----
        JSONObject addOrUpdate = new JSONObject();      //Main JSON Object
        JSONArray sheets = new JSONArray();             //Array for all Item Objects

        //Primary Key for BatchDocumentBody (Array Sheets)
        addOrUpdate.put("addOrUpdate", sheets);

        for (int i=0, i<5, i++){
            JSONObject item = new JSONObject();         //Individual Sheet object
            item.put("documentId", "" +                                                                             //REPLACE WITH URL
            rs.getString("SHEET_ID"));
            item.put("ref_num", rs.getString("SHEET_REF_NUM"));
            item.put("work_order_num", rs.getString("WORK_REQUEST_ID"));
            item.put("premise_id", rs.getString("PREMISE"));
            item.put("municipality", rs.getString("MUNICIPALITY"));
            item.put("civic_num", rs.getString("CIVIC_NUM"));
            item.put("address", rs.getString("ADDRESS"));
            item.put("job_type", rs.getString("JOB_TYPE_ID"));
            item.put("asset_id", rs.getString("PMTS_ASSET_ID"));
            item.put("image_name", rs.getString("IMAGE_NAME"));
            item.put("area_id", rs.getString("AREA_ID"));
            item.put("Data", "Address = " + rs.getString("ADDRESS") + ", " + rs.getString("MUNICIPALITY") +
            "     Asset ID = " + rs.getString("PMTS_ASSET_ID") + "      Premise ID = " +
            rs.getString("PREMISE") + "      Image Name = " + rs.getString("IMAGE_NAME") +
            "      Area ID = " + rs.getString("AREA_ID"));

            String extension = FilenameUtils.getExtension(filePath);
            // add period to extension; use .txt if no extension was found
            if (StringUtils.isBlank(extension)) {
                extension = ".txt";
            } else {
                extension = "." + extension;
            }
            item.put("FileExtension", extension);

            //Add the sheet to the "sheets" array
            sheets.add(item);

        }

		try (FileWriter file = new FileWriter("")) {                                    /* REPLACE WITH DEST DIRECTORY */
			file.write(addOrUpdate.toJSONString());
			System.out.println(addOrUpdate);
		}
	}
}


PUT https://push.cloud.coveo.com/v1/organizations/<MyOrganizationId>/sources/<MySourceId>/documents/batch?fileId=<MyFileId> HTTP/1.1
 
Content-Type: application/json
Authorization: Bearer <MyAccessToken>

https://push.cloud.coveo.com/v1/organizations/{organizationId}/sources/{sourceId}/documents/batch

URL url = new URL("https://push.cloud.coveo.com/v1/organizations/" + organizationId + "/sources/" + sourceId + "/documents?documentId=" + URLEncoder.encode(docId, "UTF-8"));








public AbstractMap.SimpleEntry<String, String> getS3File() throws Exception {

    URL url = new URL("https://push.cloud.coveo.com/v1/organizations/" + organizationId + "/files");
    LOGGER.info("=>> Getting S3 File Info: " + url.toString());

    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

    // Add the Authorization header with the accessToken
    conn.setRequestProperty("Authorization", "Bearer " + accessToken);

    // Set parameters of the HttpURLConnection
    conn.setReadTimeout(10000);
    conn.setConnectTimeout(15000);
    conn.setRequestMethod("POST");
    conn.setDoInput(true);
    conn.setDoOutput(true);

    // Open the URL connection
    conn.connect();

    int responseCode = conn.getResponseCode();
    LOGGER.info("<<= Response Code = " + responseCode + " " + conn.getResponseMessage());

    if (responseCode == 201) {
        // If successful, read the response, which contains JSON information
        InputStream is = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String output = reader.readLine();
        LOGGER.info("<<= Response Body: " + output);

        // Parse the JSON
        JSONTokener tokener = new JSONTokener(output);
        JSONObject root = new JSONObject(tokener);

        // Extract the uploadUri and fileId
        String uploadUri = root.getString("uploadUri");
        String fileId = root.getString("fileId");

        // return those two values
        return new AbstractMap.SimpleEntry<>(uploadUri, fileId);
    } else {
        // Else the request failed in some way. Check the error stream for information
        InputStream is = conn.getErrorStream();
        LOGGER.severe("Error = " + readStream(is));
        throw new Exception("Unable to fetching S3 URL");
    }
}


    /**
     * PUT the provided JSON Document on Coveo
     */
    public void putBatchOfDocumentOnCoveo(String json) throws Exception {
        try {
            // append docId (url-encoded to be safe)
            URL url = new URL("https://push.cloud.coveo.com/v1/organizations/" + organizationId + "/sources/" + sourceId + "/documents?documentId=" + URLEncoder.encode(docId, "UTF-8"));
            LOGGER.info("=>> PUT document to Coveo: " + url.toString());
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            // Set authorization and content-type headers
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("content-type", "application/json");

            // Set parameters of the HttpURLConnection
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            BufferedOutputStream bos = new BufferedOutputStream(conn.getOutputStream());
            BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
            int i;
            // read byte by byte until end of stream
            while ((i = bis.read()) >= 0) {
                bos.write(i);
            }
            bos.close();

            int responseCode = conn.getResponseCode();
            LOGGER.info("<<= Response Code = " + responseCode + " " + conn.getResponseMessage());

            if (responseCode != 202) {
                // If the request failed in some way. Check the error stream for information
                InputStream is = conn.getErrorStream();
                LOGGER.severe("Error = " + readStream(is));
                throw new Exception("Unable to upload JSON Document to Coveo");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
