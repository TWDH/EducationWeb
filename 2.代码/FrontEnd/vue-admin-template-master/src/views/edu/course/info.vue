<template>

  <div class="app-container">

    <h2 style="text-align: center;">Publish New Movie</h2>

    <el-steps :active="1" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="Edit Movie Info"/>
      <el-step title="Create Episode"/>
      <el-step title="Publish"/>
    </el-steps>

    <!-- 提交表单 -->
    <el-form label-width="120px">
        <el-form-item label="Movie Title">
            <el-input v-model="courseInfo.title" placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写"/>
        </el-form-item>

        <!-- 所属分类 -->
        <el-form-item label="Movie Category">
            <!-- 一级分类 -->
            <el-select
                v-model="courseInfo.subjectParentId"
                placeholder="Category One" @change="subjectLevelOneChanged">

                <!-- 遍历后端查找的所有讲师 -->
                <el-option
                v-for="subject in subjectOneList"
                :key="subject.id"
                :label="subject.title"
                :value="subject.id"/>
            </el-select>

            <!-- 二级分类 -->
            <el-select v-model="courseInfo.subjectId" placeholder="Category Two">
                <el-option
                    v-for="subject in subjectTwoList"
                    :key="subject.id"
                    :label="subject.title"
                    :value="subject.id"/>
            </el-select>

        </el-form-item>

        <!-- 课程讲师 -->
        <el-form-item label="Artist">
            <el-select
                v-model="courseInfo.teacherId"
                placeholder="Choose">

                <!-- 遍历后端查找的所有讲师 -->
                <el-option
                v-for="teacher in teacherList"
                :key="teacher.id"
                :label="teacher.name"
                :value="teacher.id"/>
            </el-select>
        </el-form-item>

        <el-form-item label="Hours">
            <el-input-number :min="0" v-model="courseInfo.lessonNum" controls-position="right" placeholder="请填写课程的总课时数"/>
        </el-form-item>

        <!-- 课程简介 -->
        <el-form-item label="Introduction">
            <tinymce :height="300" v-model="courseInfo.description"/>
        </el-form-item>

        <!-- 课程封面 -->
        <el-form-item label="Movie Cover">

        <el-upload
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :action="BASE_API+'/eduoss/fileoss'"
            class="avatar-uploader">
            <img :src="courseInfo.cover">
        </el-upload>

        </el-form-item>

        <!-- 课程价格 -->
        <el-form-item label="Price">
            <el-input-number :min="0" v-model="courseInfo.price" controls-position="right" placeholder="免费课程请设置为0元"/> CAD
        </el-form-item>

        <el-form-item>
            <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">Save and next</el-button>
        </el-form-item>
    </el-form>

  </div>
</template>

<script>
import course from '@/api/edu/course'
import subject from '@/api/edu/subject'
import Tinymce from '@/components/Tinymce'

export default {
  //声明组件
  components: { Tinymce },

  data() {
    return {
      saveBtnDisabled: false, // 保存按钮是否禁用
      courseInfo:{
          title: '',
          subjectId: '',
          subjectParentId:'',
          teacherId: '',
          lessonNum: 0,
          description: '',
          cover: 'static/01.jpg',
          price: 0
      },
      courseId:'', //课程id值
      teacherList:[], //封装所有讲师
      subjectOneList:[], //一级分类
      subjectTwoList:[], //二级分类
      BASE_API: process.env.BASE_API, //接口api地址
    }
  },

  created() {
    this.init()
  },

   watch: { //监听
    $route(to, from) {
      this.init()
    }
  },

  methods: {
    //初始化
    init(){
        //获取课程id值
        if(this.$route.params && this.$route.params.id){
            //1.修改Course操作

            this.courseId = this.$route.params.id
            //1.1 调用根据id查询课程的方法，返回并赋值this.courseInfo
            this.getInfo()

        }else{
            //2.添加Course操作
            //清空数据
            this.courseInfo = {
                title: '',
                subjectId: '',
                subjectParentId:'',
                teacherId: '',
                lessonNum: 0,
                description: '',
                cover: 'static/01.jpg',
                price: 0
            }
            //初始化所有讲师
            this.getListTeacher()
            //初始化一级分类
            this.getOneSubject()
        }
    },  

    //根据id查询Course属性所有信息
    getInfo(){
        course.getCourseInfoId(this.courseId)
            .then(response => {
                //1.1 courseInfo课程基本信息，包含一级分类id，和二级分类id（subjectTwoList没有值）
                this.courseInfo = response.data.courseInfoVo

                //1.2 查询所有分类，包括一级和二级（解决回显二级目录没有值的问题）
                subject.getSubjectList()
                    .then(response => {
                        //1.2.1 获取所有一级分类
                        this.subjectOneList = response.data.list
                        //1.2.2 把所有一级分类进行遍历
                        for(var i = 0; i < this.subjectOneList.length; i++){
                            //1.2.2.1 获取每一个一级分类
                            var oneSubject = this.subjectOneList[i]
                            //1.2.2.2 比较当前"courseInfo中一级分类id"和"所有一级分类id"
                            if(this.courseInfo.subjectParentId == oneSubject.id){
                                //1.2.2.2.1 获取该一级分类下的所有二级分类
                                this.subjectTwoList = oneSubject.children
                            }
                        }
                        //初始化所有讲师（下拉菜单）
                        this.getListTeacher()
                    })
            })
    },  

    //上传封面成功调用方法
    handleAvatarSuccess(res, file) {
        this.courseInfo.cover = res.data.url
    },
    //上传之前调用的方法
    beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg'
        const isLt2M = file.size / 1024 / 1024 < 2

        if (!isJPG) {
            this.$message.error('上传头像图片只能是 JPG 格式!')
        }
        if (!isLt2M) {
            this.$message.error('上传头像图片大小不能超过 2MB!')
        }
        return isJPG && isLt2M
    },


    //点击一级分类，触发change，显示对应二级分类
    subjectLevelOneChanged(value){
        //value：当前点击的一级分类的id（框架封装）
        //遍历所有分类，包含一级和二级
        for(var i = 0; i < this.subjectOneList.length; i++){
            //每个一级分类
            var oneSubject = this.subjectOneList[i]
            //判断："所有一级分类id" 和 "点击的一级分类id" 是否一样
            if(value === oneSubject.id){
                //从一级分类获取里面所有的二级分类
                this.subjectTwoList = oneSubject.children
                //二级分类id值清空
                this.courseInfo.subjectId =''
            }
        }
    },  

    //查询所有一级分类
    getOneSubject(){
        subject.getSubjectList()
            .then(response => {
                this.subjectOneList = response.data.list
            })
    },  

    //查询所有讲师
    getListTeacher(){
        course.getListTeacher()
            .then(response => {
                this.teacherList = response.data.items
            })
    },

    //添加课程
    addCourse(){
      course.addCourseInfo(this.courseInfo)
        .then(response => {
            //提示
            this.$message({
                type: 'success',
                message: '添加课程信息成功'
            });
            //跳转到第二步, 取后端传过来的课程id
            this.$router.push({ path: '/course/chapter/'+ response.data.courseId})
        })
    },

    //修改课程
    updateCourse(){
        course.updateCourseInfo(this.courseInfo)
            .then(response => {
                //提示
                this.$message({
                    type: 'success',
                    message: '修改课程信息成功'
                });
                //跳转到第二步, 取后端传过来的课程id
                this.$router.push({ path: '/course/chapter/'+ this.courseInfo.id})
            })
    },

    //添加/修改课程信息
    saveOrUpdate() {
      if(!this.courseInfo.id ){
          //添加
          this.addCourse()
      }
      else{
          //修改
          this.updateCourse()
      }
    }

  }
}
</script>


<style scoped>
.tinymce-container {
  line-height: 29px;
}
</style>