package org.studyeasy.sptingBlog.util.constants;

public enum Privilages {
    RESET_ANY_USER_PASSWORD(1l,"RESET_ANY_USER_PASSWORD"),
    ACCESS_ADMIN_PANEL(2l,"ACCESS_ADMIN_PANEL");

    private Long privilageId;
    private String privilageString;
    private Privilages(Long id, String string){
        this.privilageId = id;
        this.privilageString = string;
    } 

    public Long getPrivilageId(){
        return privilageId;
    }

    public String getPrivilageString(){
        return privilageString;
    }

}
