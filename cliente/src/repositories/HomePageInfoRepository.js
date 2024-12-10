import HTTP from "@/common/http";

export default {
    async getHomePageInfo() {
        const response = await HTTP.get("/home");
        return response.data;
    },

    async updateHomePageInfo(info) {
        return await HTTP.post("/update", info);
    },
};