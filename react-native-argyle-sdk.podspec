require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-argyle-sdk"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-argyle-sdk
                   DESC
  s.homepage     = "https://argyle.io"
  s.license      = "Apache 2.0"
  s.authors      = { "Argyle Systems" => "argyle@argyle.io" }
  s.platforms    = { :ios => "11.0", :tvos => "11.0" }
  s.source       = { :git => "https://github.com/github_account/react-native-argyle-sdk.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency 'Argyle', '~> 1.1'
end

