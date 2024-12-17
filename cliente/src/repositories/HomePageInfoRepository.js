import HTTP from "@/common/http";

export default {
    async getHomePageInfo() {
        const response = await HTTP.get("/home");
        return response.data;
    },

    async updateHomePageInfo(formData) {
        return await HTTP.post("/update", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });
      },
};