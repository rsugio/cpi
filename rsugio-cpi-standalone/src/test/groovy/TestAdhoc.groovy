import org.junit.Test

class TestAdhoc {

    @Test
void "Вот и славно, трампампам!"() {
        String body = getClass().getResourceAsStream("xml.xml").text
        String channelHH = "81"
        String channelSJ = "82"
        def idsHH = [23704398, 23586288, 23087775, 23564463, 23431413, 23168903, 22735357, 23233806, 23399947, 23330063, 23330008, 23192698, 22819233, 22819223, 22894643, 22513548, 22377451, 22262806, 22551991, 22379843, 18278911, 18278924, 22409690, 22434200, 22098944, 22371912, 22262781, 21858626, 21825808, 21914249, 21940777, 21914248, 21938456, 21086468, 20833504, 23790339, 23783020, 23781409, 23761654, 23760232, 23702831, 23663687, 23663342, 23732039, 23745122, 23428686, 23428681, 23568446, 23407505, 23361580, 23305649, 23599665, 23228499, 23121506, 23100866, 22714426, 22094422, 23246644, 23146522, 23185727, 23043697, 22716891, 22797991, 22497304, 22384417, 22384404, 22262178, 22260315, 22245902, 22086115, 22004636, 21944703, 22141376, 21417655, 21300378, 23800927, 23155992, 23378974, 21425004, 21424253, 21424168, 23804982, 23804621, 23804333, 23834818, 23349382, 23368649, 23522151, 22322276, 22601594, 22868451, 22600083, 22101955, 22713863, 22919646, 22506980, 22713883, 22086380, 22175107, 22322280, 22322287, 22322262, 22004060, 21916824, 21916437, 21915565, 21915511, 21915244, 21915149, 21915038, 21914687, 23886994, 23248403, 22898394, 22898390, 21419674, 20836390, 21140137, 21088912, 23758568, 23601454, 23580224, 23433728, 23380151, 23380134, 23044336, 23155563, 23155562, 23155561, 23080993, 23080674, 23392497, 22993078, 22993124, 23034069, 23225068, 22951464, 22934687, 22637979, 22784715, 22784708, 22712875, 22752137, 22544798, 22182003, 22525759, 22432950, 22411186, 22382087, 22382158, 21953803, 22309408, 21911243, 22181363, 22177587, 22107414, 22107247, 21912929, 21911370, 22092818, 21873130, 20894551, 22045770, 21407687, 20050276, 20927615, 20931063, 20894541, 20927975, 20927774, 20927857, 20931049, 20894557, 20827741, 20751434, 23997518, 23250809, 23099001, 22729289, 22730743, 22022642]
        def idsSJ = [30662729, 30505993, 28489744, 30383106, 30318289, 30686526, 30685905, 30504445, 28251129, 30611745, 29424004, 29730636, 29682779, 29252712, 30268618, 30488679, 30639346, 30565063, 29754433, 29310774, 25888787, 30587324, 30531173, 30447716, 29092278, 30530815, 30530598, 28974887, 30530346, 30528139, 30572330, 30527889, 30614237, 29517550, 29595014, 30160495, 30160400, 29404867, 29848616, 30688541, 30608797, 30608789, 30608785, 30608783, 30650165, 30650131, 30608668, 30608667, 29543309, 30608438, 29452905, 30526703, 30086968, 30228384, 27006678, 30593602, 30447650, 30607731, 30526244, 29516686, 29410118, 28693047, 29487200, 27117249, 30607285, 29581254, 30525836, 30406657, 30396843, 29582967, 30665073, 30528047, 30520575, 30520576, 30520577, 30520590, 28825777, 30600363, 29921788, 30600232, 30519287, 30626761, 30518427, 30518237, 30518106, 30098153, 30220020, 30014002, 30216510, 30268490, 29925891, 29814934, 30593907, 30593777, 29371419, 30603558, 30558899, 30513371, 30513312, 30593112, 30593106, 30588311, 28260846, 30696477, 26869693, 30591067, 29899198, 30420412, 30566021, 30590998, 29787477, 30510756, 30590249, 29437500, 30216407, 28307806, 27868179, 29269332, 28462837, 28813842, 29044819, 30654225, 30647509, 30681677, 28209405, 29916753, 30608643, 30687492, 30484380, 28787513, 28465240, 27599805, 30681694, 30479853, 30304581, 30582449, 30220752, 30478925, 30556162, 30478169, 30030874, 30242243, 30504019, 30475964, 30475900, 30475738, 30503036, 30502995, 30367571, 30295511, 30473998, 29816959, 30473315, 29364657, 30499933, 30499867, 29373258, 30579279, 28495566, 30268088, 30578170, 30495382, 30467950, 30575813, 29630226, 29256795, 30467527, 30467067, 30574692, 30466853, 29515331, 30466073, 30465661, 29976430, 30505999, 29605821, 30492063, 30674500, 30569998, 29822224, 30598053, 30568701, 30424582, 30032207, 30568438, 30265113, 30566908, 30566463, 29429415, 29420630, 29510612, 29389390, 30566345, 29876352, 30321752, 30304378, 30503672, 30359534, 28051976, 30092103, 30394240, 30114628, 30114661, 30300220, 30355303, 29449921, 30530789, 30316106, 29210520, 29130733, 30663665, 29700074, 30645115, 28978896, 30350363, 30591872, 30488062, 30452285, 30452284, 30617742, 30617738, 30617727, 30617724, 30617720, 30617718, 30617714, 30617712, 30617705, 30617700, 30451564, 30559260, 30185358, 27234993, 29404865, 30451021, 30032185, 29500087, 30638720, 30558241, 30449190, 30199156, 30182355, 30556582, 28504539, 28504556, 30447715, 30447710, 30447700, 30447692, 30447675, 30447676, 30447518, 30447515, 30447462, 30447458, 30447459, 30447460, 30447461, 30447425, 30447415, 30447386, 30447380, 30447366, 30447220, 30447202, 29767438, 30447059, 30446782, 30446518, 30241614, 30580036, 30591444, 29260379, 30608816, 30608788, 30608666, 30558853, 29656055, 28657006, 29409804, 29826884, 29779054, 30513234, 30654314, 30568792, 30548170, 30568699, 30568618, 30653790, 30547984, 30547833, 29920973, 30484461, 30520591, 29379158, 30546616, 30551400, 30465692, 30551046, 30566360, 30447694, 30447645, 30447648, 30447464, 30447354, 30608975, 30522815, 30544396, 29967672, 30306831, 30518296, 30518210, 30518211, 30649359, 29967665, 30322204, 30540893, 30458410, 30458415, 30458391, 30388757, 30539144, 30639995, 30537773, 29521450, 30644127, 29611685, 29363967, 30642757, 30642669, 30504063, 30642556, 30642451, 30642342, 30531461, 29755301, 29988390, 30541599, 30587844, 30566468, 30541087, 29448894, 30437608, 29958615, 30436694, 30570307, 30570290, 30581110, 30343726, 30587505, 30540652, 30258271, 30557681, 30563288, 30530597, 30530126, 30529996, 29998726, 28587337, 30039647, 28586894, 30527888, 29921769, 30527034, 30526701, 30526611, 30526608, 30425100, 29600804, 30424271, 30425180, 30423160, 30519957, 30519954, 30427182, 30330603, 29628981, 30530705, 30522012, 30522001, 29747776, 29386862, 30417957, 30596042, 30092093, 30596035, 30614286, 30562015, 30518395, 29751267, 29936099, 30111834, 30579756, 30307734, 27662528, 30306805, 30617463, 30156170, 27644555, 30512488, 30484542, 30307691, 29072574, 28908982, 30561430, 30412492, 30412086, 30412020, 30411970, 30411899, 30411765, 30411678, 30411531, 30411417, 30407636, 30407178, 30407012, 30278366, 30500394, 30518223, 30530721, 30513462, 30380689, 30402259, 30402197, 30402102, 30402022, 30401940, 30401839, 30548030, 30547961, 30401721, 30547935, 30401615, 30547711, 30401494, 29341028, 30401306, 30498756, 28508329, 30449351, 30520529, 30273645, 28878279, 30095353, 30502687, 30465691, 30566484, 30465657, 30566408, 30550793, 30550792, 30447894, 30447714, 30447656, 30447635, 30501041, 30482380, 30518139, 30513474, 30513427, 30525000, 30092633, 29364649, 29543441, 29346872, 30587403, 30558920, 30499887, 29764446, 30572564, 30492480, 29763975, 30492231, 28187025, 28005732, 30471561, 29127214, 30427477, 30591348, 30528157, 27907962, 30447130, 30394812, 30481952, 30389736, 30556484, 30038164, 30481301, 30429646, 30200818, 25923557, 30385200, 30385097, 30382367, 30382077, 30484433, 30255670, 30377560, 29344014]

        String hostSF = "https://api18sf"
        List<String> log = ["Deletion prepare started, idsSJ size=${idsSJ.size()}, idsHH size=${idsHH.size()}"]
        def delete = []
        def ban = ['jobReqId_jobPostingId'] //magic but works
        def rootNode = new XmlSlurper().parseText(body)

        rootNode.children().each{
            String channelId = it.channelId as String
            String postingStatus = it.postingStatus as String	// Deleted,Updated,Success,Expired
            String jobPostingId = it.jobPostingId
            String jobReqId = it.jobReqId
            String custHH = it.jobRequisition.JobRequisition.custHH as String
            String custSJ = it.jobRequisition.JobRequisition.custSJ as String
            String lastModifiedDateTime = it.lastModifiedDateTime as String

            String magic = "${jobReqId}_${jobPostingId}"
            if (channelId==null || channelId.isEmpty() || '2017-07-01' > lastModifiedDateTime) {
//			log.add('internal agency')
            } else if (channelId == channelHH) {
                if (custHH==null || custHH.isEmpty() || custHH.contains('.')) {
//					do nothing: HH is wrong
                } else if (postingStatus=='Deleted') {
                    ban.add(magic)
                } else if (postingStatus!='Deleted' && (custHH as String) in idsHH && !(magic in ban)) {
                    String z = "${hostSF}/odata/v2/JobRequisitionPosting(jobPostingId=${jobPostingId}L,jobReqId=${jobReqId}L)"
                    delete.add(z)
                    log.add("For custHH=$custHH with status '$postingStatus' need to delete $z")
                }
            } else if (channelId == channelSJ) {
                if (custSJ==null || custSJ.isEmpty() || custSJ.contains('.')) {
//					do nothing: "SJ - wrong"
                } else if (postingStatus=='Deleted') {
                    ban.add(magic)
                } else if (postingStatus!='Deleted' && (custSJ as String) in idsSJ && !(magic in ban)) {
                    String z = "${hostSF}/odata/v2/JobRequisitionPosting(jobPostingId=${jobPostingId}L,jobReqId=${jobReqId}L)"
                    delete.add(z)
                    log.add("For custSJ=$custSJ with status '$postingStatus' need to delete $z")
                }
            }
        }

        println log.join('\n')
    }
}
